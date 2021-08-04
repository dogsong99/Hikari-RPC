package com.dogsong.rpc.config;

import com.dogsong.rpc.annotation.HikariRpcService;
import com.dogsong.rpc.common.codec.Decoder;
import com.dogsong.rpc.common.codec.Encoder;
import com.dogsong.rpc.model.Request;
import com.dogsong.rpc.model.Response;
import com.dogsong.rpc.registry.RegistryServer;
import com.dogsong.rpc.server.BeanFactory;
import com.dogsong.rpc.server.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 提供方 注册服务
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
@Configuration
@ConditionalOnClass(HikariRpcService.class)
public class ProviderAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(ProviderAutoConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HikariProperties properties;

    @PostConstruct
    public void init() {
        logger.info("hikari-rpc server start scanning provider service...");
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(HikariRpcService.class);
        if (beans.size() > 0) {
            beans.entrySet().forEach(bean -> {
                // 初始化 provider
                initProviderBean(bean.getKey(), bean.getValue());
            });
        }
        logger.info("hikari-rpc server scan over...");
        // 如果有服务的话才启动 netty server
        if (beans.size() > 0) {
            startNetty(properties.getPort());
        }
    }

    /**
     * 初始化服务（被调用方）
     *
     * @param beanName beanName
     * @param bean bean
     */
    private void initProviderBean(String beanName, Object bean) {
        HikariRpcService service = this.applicationContext.findAnnotationOnBean(beanName, HikariRpcService.class);
        assert service != null;
        BeanFactory.addBean(service.value(), bean);
    }

    /**
     * 启动 netty 服务
     *
     * @param port 服务端口
     */
    public void startNetty(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new Decoder(Request.class),
                                    new Encoder(Response.class),
                                    new ServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            logger.info("server started on port : {}", port);
            // netty服务端启动成功后，向zk注册这个服务
            logger.info("registry zk addr is: {} ...", properties.getRegisterAddress());
            new RegistryServer(properties.getRegisterAddress(),
                    properties.getTimeout(), properties.getServerName(),
                    properties.getHost(), port)
                    .register();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
