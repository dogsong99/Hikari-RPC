package com.dogsong.rpc.config.spring.bean;

import com.alibaba.fastjson.JSON;
import com.dogsong.rpc.common.LocalServerInfo;
import com.dogsong.rpc.common.RpcRegisterConfig;
import com.dogsong.rpc.config.spring.ProviderConfig;
import com.dogsong.rpc.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/29
 */
public class ProviderBean extends ProviderConfig implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ProviderBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        RpcRegisterConfig rpcProviderConfig = new RpcRegisterConfig();
        rpcProviderConfig.setNozzle(nozzle);
        rpcProviderConfig.setReference(ref);
        rpcProviderConfig.setAlias(alias);
        rpcProviderConfig.setHost(LocalServerInfo.LOCAL_HOST);
        rpcProviderConfig.setPort(LocalServerInfo.LOCAL_PORT);

        //注册生产者
        long count = RedisRegistryCenter.registryProvider(nozzle, alias, JSON.toJSONString(rpcProviderConfig));

        logger.info(String.format("注册生产者：%s %s %s", nozzle, alias, count));
    }

}