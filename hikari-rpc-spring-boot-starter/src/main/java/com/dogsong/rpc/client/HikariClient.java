package com.dogsong.rpc.client;


import com.dogsong.rpc.common.codec.Decoder;
import com.dogsong.rpc.common.codec.Encoder;
import com.dogsong.rpc.model.Request;
import com.dogsong.rpc.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 客户端处理类
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
public class HikariClient extends SimpleChannelInboundHandler<Response> {

    private final Logger logger = LoggerFactory.getLogger(HikariClient.class);

    /** 服务端ip */
    private final String host;

    /** 服务端端口号 */
    private final int port;

    /** 异步调用结果 */
    private CompletableFuture<String> future;

    /** 接受服务端返回结果 */
    private Response response;

    public HikariClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * netty 通讯发送
     *
     * @param request 请求
     * @param timeOut 超时时间
     * @return {@link Response}
     */
    public Response send(Request request, int timeOut) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    // 转码
                                    new Encoder(Request.class),
                                    // 解码
                                    new Decoder(Response.class),
                                    HikariClient.this
                            );
                        }
                    }).option(ChannelOption.SO_KEEPALIVE, true);
            // 连接 netty 服务器
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().writeAndFlush(request).sync();
            future = new CompletableFuture<>();
            future.get(timeOut, TimeUnit.MILLISECONDS);
            if (response != null) {
                // 关闭 netty 连接
                channelFuture.channel().closeFuture().sync();
            }
            return response;
        } catch (Exception e) {
            logger.error("client send msg error,", e);
            return null;
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) {
        logger.info("client get request result,{}", response);
        this.response = response;
        future.complete("");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("netty client caught exception,", cause);
        ctx.close();
    }
}
