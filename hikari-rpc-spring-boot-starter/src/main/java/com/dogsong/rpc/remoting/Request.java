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
public class Request {

    private transient Channel channel;

    /** 唯一键 */
    private String requestId;

    /** 方法 */
    private String methodName;

    /** 属性 */
    private Class[] paramTypes;

    /** 入参 */
    private Object[] args;

    /** 接口 */
    private String nozzle;

    /** 实现类 */
    private String ref;

    /** 别名 */
    private String alias;

}
