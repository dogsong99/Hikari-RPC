package com.dogsong.rpc.remoting.client;

import com.dogsong.rpc.remoting.Request;
import com.dogsong.rpc.remoting.Response;
import com.dogsong.rpc.remoting.codec.Decoder;
import com.dogsong.rpc.remoting.codec.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

/**
 * 客户端连接
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class ClientSocket implements Runnable{

    private final Logger logger = Logger.getLogger(ClientSocket.class);

    private ChannelFuture channelFuture;

    private final String inetHost;

    private final int inetPort;

    public ClientSocket(String inetHost, int inetPort) {
        this.inetHost = inetHost;
        this.inetPort = inetPort;
    }

    @Override
    public void run() {
        logger.info(String.format("正在连接远程服务端: %s : %s", inetHost, inetPort));
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.AUTO_READ, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            // 解码
                            new Decoder(Response.class),
                            // 编码
                            new Encoder(Request.class),
                            // 收发消息
                            // 超时处理类
                            new ClientHandler());
                }
            });
            // 点对点连接
            ChannelFuture future = bootstrap.connect(inetHost, inetPort).sync();
            logger.info("服务连接成功");
            this.channelFuture = future;
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }
}
