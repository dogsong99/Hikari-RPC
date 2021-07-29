package com.dogsong.rpc.config;

import com.dogsong.rpc.registry.RedisRegistryCenter;
import com.dogsong.rpc.remoting.server.ServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;


/**
 * 注册中心配置加载
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/28
 */
public class ServerAutoConfiguration implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(ServerAutoConfiguration.class);

    @Resource
    private ServerProperties serverProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("启动 Redis模拟注册中心开始");
        RedisRegistryCenter.init(serverProperties.getHost(), serverProperties.getPort());
        logger.info("启动 Redis模拟注册中心完成，{} {}", serverProperties.getHost(), serverProperties.getPort());

        logger.info("初始化生产端服务开始");
        ServerSocket serverSocket = new ServerSocket(applicationContext, serverProperties.getHikariPort());
        new Thread(serverSocket).start();
        while (!serverSocket.isActiveSocketServer()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignore) {
            }
        }

        logger.info("初始化生产端服务开始");
    }
}
