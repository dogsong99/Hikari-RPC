package com.dogsong.rpc.config.spring.bean;

import com.alibaba.fastjson.JSON;
import com.dogsong.rpc.common.RpcRegisterConfig;

import com.dogsong.rpc.config.spring.ConsumerConfig;
import com.dogsong.rpc.proxy.JDKProxy;
import com.dogsong.rpc.registry.RedisRegistryCenter;
import com.dogsong.rpc.remoting.Request;
import com.dogsong.rpc.remoting.client.ClientSocket;
import com.dogsong.rpc.utils.ClassLoaderUtil;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;


/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/29
 */
public class ConsumerBean<T> extends ConsumerConfig implements FactoryBean {

    private final Logger logger = LoggerFactory.getLogger(ConsumerBean.class);

    private ChannelFuture channelFuture;

    private RpcRegisterConfig registerConfig;

    @Override
    public Object getObject() throws Exception {
        // redis 中获取被调用信息
        if (null == registerConfig) {
            String provider = RedisRegistryCenter.obtainProvider(nozzle, alias);
            registerConfig = JSON.parseObject(provider, RpcRegisterConfig.class);
        }
        assert null != registerConfig;

        // 获取 channel
        if (null == channelFuture) {
            ClientSocket clientSocket = new ClientSocket(registerConfig.getHost(), registerConfig.getPort());
            new Thread(clientSocket).start();
            // 等待线程处理
            while (null == channelFuture) {
                Thread.sleep(500);
                channelFuture = clientSocket.getChannelFuture();
            }
        }

        Request request = new Request();
        request.setChannel(channelFuture.channel());
        request.setNozzle(nozzle);
        request.setRef(registerConfig.getReference());
        request.setAlias(alias);

        return (T) JDKProxy.getProxy(ClassLoaderUtil.forName(nozzle), request);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return ClassLoaderUtil.forName(nozzle);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
