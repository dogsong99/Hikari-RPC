package com.dogsong.rpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/28
 */
@ConfigurationProperties("rpc.server")
public class ServerProperties {

    /** 注册中心地址 */
    private String host;

    /** 注册中心端口 */
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
