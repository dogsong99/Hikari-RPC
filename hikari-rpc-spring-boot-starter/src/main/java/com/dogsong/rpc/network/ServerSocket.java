package com.dogsong.rpc.network;

import com.dogsong.rpc.domain.LocalServerInfo;
import com.dogsong.rpc.utils.NetUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationContext;

/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/28
 */
public class ServerSocket implements Runnable {

    private ChannelFuture channelFuture;

    private transient ApplicationContext applicationContext;

    public ServerSocket(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public boolean isActiveSocketServer() {
        try {
            if (channelFuture != null) {
                return channelFuture.channel().isActive();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    // new RpcDecoder(Request.class),
                                    // new RpcEncoder(Response.class),
                                    // new MyServerHandler(applicationContext));
                            );
                        }
                    });
            // 启动初始端口
            int port = 17011;

            while (NetUtil.isPortUsing(port)) {
                port++;
            }
            LocalServerInfo.LOCAL_HOST = NetUtil.getHost();
            LocalServerInfo.LOCAL_PORT = port;

            //注册服务
            this.channelFuture = b.bind(port).sync();
            this.channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}