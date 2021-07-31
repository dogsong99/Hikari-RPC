package com.dogsong.rpc.remoting.codec;

import com.dogsong.rpc.utils.ProtostuffSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class Decoder extends ByteToMessageDecoder {

    private final Class<?> genericClass;

    public Decoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        // 将 ByteBuf 转换为 byte[]
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        // 将 data 转换成 object
        Object obj = ProtostuffSerialization.deserialize(data, genericClass);
        out.add(obj);
    }
}