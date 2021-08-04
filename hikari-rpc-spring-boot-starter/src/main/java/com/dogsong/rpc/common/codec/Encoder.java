package com.dogsong.rpc.common.codec;

import com.dogsong.rpc.util.ProtostuffSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 转码器
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
public class Encoder extends MessageToByteEncoder {

    private final Class<?> genericClass;

    public Encoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 序列化
        if (genericClass.isInstance(msg)) {
            byte[] data = ProtostuffSerialization.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
