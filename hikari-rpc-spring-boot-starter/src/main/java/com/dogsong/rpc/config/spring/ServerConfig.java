package com.dogsong.rpc.config.spring;

/**
 * TODO
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class ServerConfig {
    protected String host;  //注册中心地址
    protected int port;     //注册中心端口

    protected int hikariPort;     //注册中心端口

    public int getHikariPort() {
        return hikariPort;
    }

    public void setHikariPort(int hikariPort) {
        this.hikariPort = hikariPort;
    }

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
