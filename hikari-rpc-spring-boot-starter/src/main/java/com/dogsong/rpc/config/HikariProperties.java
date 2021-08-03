package com.dogsong.rpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * server 配置
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/28
 */
@ConfigurationProperties("hikari.rpc")
public class HikariProperties {

    /** zk的地址 */
    private String registerAddress = "server:2181";

    /** rpc服务的端口 */
    private int port = 21810;

    /** 服务名称 */
    private String serverName = "rpc";

    /** 服务地址 */
    private String host = "localhost";

    /** 超时时间 */
    private int timeout = 2000;

    public String getRegisterAddress() {
        return registerAddress;
    }

    public HikariProperties setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
        return this;
    }

    public int getPort() {
        return port;
    }

    public HikariProperties setPort(int port) {
        this.port = port;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public HikariProperties setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getHost() {
        return host;
    }

    public HikariProperties setHost(String host) {
        this.host = host;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public HikariProperties setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
