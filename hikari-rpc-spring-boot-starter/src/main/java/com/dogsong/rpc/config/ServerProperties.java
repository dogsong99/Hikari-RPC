package com.dogsong.rpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * server 配置
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/28
 */
@Data
@ConfigurationProperties("rpc.server")
public class ServerProperties {

    /** 注册中心地址 */
    private String host;

    /** 注册中心端口 */
    private int port;

    /** rpc 端口 */
    private int hikariPort;
}
