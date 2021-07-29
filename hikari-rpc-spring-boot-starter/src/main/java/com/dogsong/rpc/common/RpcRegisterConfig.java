package com.dogsong.rpc.common;

import lombok.Data;

/**
 * RPC Register config
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
@Data
public class RpcRegisterConfig {

    /** 接口 */
    private String nozzle;

    /** 映射 */
    private String reference;

    /** 别名 */
    private String alias;

    /** ip */
    private String host;

    /** 端口 */
    private int port;

}
