package com.dogsong.rpc.config;

import com.dogsong.rpc.client.HikariProxy;
import com.dogsong.rpc.exception.ZkConnectException;
import com.dogsong.rpc.registry.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 注册中心配置加载
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/28
 */
@Configuration
@EnableConfigurationProperties(HikariProperties.class)
public class HikariAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(HikariAutoConfiguration.class);

    @Autowired
    private HikariProperties hikariProperties;

    @Bean
    @ConditionalOnMissingBean
    public ServiceDiscovery serviceDiscovery() {
        ServiceDiscovery serviceDiscovery = null;
        try {
            logger.info("启动 服务发现中心开始");
            serviceDiscovery = new ServiceDiscovery(hikariProperties.getRegisterAddress(), hikariProperties.getTimeout());
            logger.info("启动 服务发现中心完成，{}", hikariProperties.getRegisterAddress());
        } catch (ZkConnectException e) {
            logger.error("zk connect failed:", e);
        }
        return serviceDiscovery;
    }

    @Bean
    @ConditionalOnMissingBean
    public HikariProxy hikariProxy() {
        HikariProxy proxy = new HikariProxy();
        proxy.setServiceDiscovery(serviceDiscovery());
        return proxy;
    }

}
