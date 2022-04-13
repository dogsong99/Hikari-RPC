package com.dogsong.rpc.server;

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

/**
 * hikari-rpc 代理的 Bean
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
public class BeanFactory {

    /** 持有当前 hikari-rpc 服务提供的所有的服务，即有@RpcService注解的类 */
    private static final ConcurrentMap<Class<?>, Object> beans = Maps.newConcurrentMap();

    private BeanFactory(){
    }

    public static void addBean(Class<?> interfaceClass, Object bean) {
        beans.putIfAbsent(interfaceClass, bean);
    }

    public static Object getBean(Class<?> interfaceClass) {
        return beans.get(interfaceClass);
    }

}
