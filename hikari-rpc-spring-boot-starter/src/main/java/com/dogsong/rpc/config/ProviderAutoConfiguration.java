package com.dogsong.rpc.config;

import com.alibaba.fastjson.JSON;
import com.dogsong.rpc.common.LocalServerInfo;
import com.dogsong.rpc.common.RpcRegisterConfig;
import com.dogsong.rpc.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;

/**
 * 提供方 注册服务
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class ProviderAutoConfiguration implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(ProviderAutoConfiguration.class);

    @Resource
    private ProviderProperties properties;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 注册信息
        RpcRegisterConfig registerConfig = new RpcRegisterConfig();
        registerConfig.setNozzle(properties.getNozzle());
        registerConfig.setReference(properties.getReference());
        registerConfig.setAlias(properties.getAlias());
        registerConfig.setHost(LocalServerInfo.LOCAL_HOST);
        registerConfig.setPort(LocalServerInfo.LOCAL_PORT);

        // 注册生产者
        Long count = RedisRegistryCenter.registryProvider(properties.getNozzle(), properties.getAlias(), JSON.toJSONString(registerConfig));

        logger.info(String.format("注册生产者: %s %s %s .", properties.getNozzle(), properties.getAlias(), count));
    }
}
