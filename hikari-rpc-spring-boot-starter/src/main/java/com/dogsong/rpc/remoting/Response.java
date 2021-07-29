package com.dogsong.rpc.remoting;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * TODO
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
@Data
public class Response {

    private transient Channel channel;

    private String requestId;

    private Object result;
}
