package com.dogsong.rpc.client;

import com.dogsong.rpc.config.HikariProperties;
import com.dogsong.rpc.model.ProviderInfo;
import com.dogsong.rpc.model.Request;
import com.dogsong.rpc.model.Response;
import com.dogsong.rpc.registry.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 使用 JDK 代理
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
@Component
public class HikariProxy {

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Autowired
    private HikariProperties properties;

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass, String providerName) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
            // 通过 netty 向 hikari-roc 发送调用
            // 构建一个请求
            Request request = new Request();
            request.setRequestId(UUID.randomUUID().toString())
                    .setClassName(method.getDeclaringClass().getName())
                    .setMethodName(method.getName())
                    .setParamTypes(method.getParameterTypes())
                    .setParams(args);
            // 获取一个服务提供者
            ProviderInfo providerInfo = serviceDiscovery.discover(providerName);
            String[] addrInfo = providerInfo.getAddr().split(":");
            String host = addrInfo[0];
            int port = Integer.parseInt(addrInfo[1]);
            HikariClient client = new HikariClient(host, port);
            // send
            Response response = client.send(request, properties.getTimeout());
            if (response.isError()) {
                throw response.getError();
            }
            return response.getResult();
        });
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

}
