package com.dogsong.rpc.proxy;

import com.dogsong.rpc.remoting.Request;
import com.dogsong.rpc.utils.ClassLoaderUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * JDK 代理
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class JDKProxy {


    public static <T> T getProxy(Class<T> interfaceClass, Request request) throws Exception {
        InvocationHandler invocationHandler = new InvokerInvocationHandler(request);

        // 获取当前类加载器
        ClassLoader classLoader = ClassLoaderUtil.getCurrentClassLoader();

        T result = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, invocationHandler);
        return result;
    }

}
