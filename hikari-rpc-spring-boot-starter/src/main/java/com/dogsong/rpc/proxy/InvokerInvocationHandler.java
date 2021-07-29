package com.dogsong.rpc.proxy;

import com.dogsong.rpc.remoting.Request;
import com.dogsong.rpc.remoting.Response;
import com.dogsong.rpc.remoting.future.SyncWrite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class InvokerInvocationHandler implements InvocationHandler {

    private final Request request;

    public InvokerInvocationHandler(Request request) {
        this.request = request;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class[] paramTypes = method.getParameterTypes();

        String toStr = "toString";
        String hashCode = "hashCode";
        String equals = "equals";

        if (toStr.equals(methodName) && paramTypes.length == 0) {
            return request.toString();
        } else if (hashCode.equals(methodName) && paramTypes.length == 0) {
            return request.hashCode();
        } else if (equals.equals(methodName) && paramTypes.length == 1) {
            return request.equals(args[0]);
        }
        //设置参数
        request.setMethodName(methodName);
        request.setParamTypes(paramTypes);
        request.setArgs(args);
        request.setRef(request.getRef());
        Response response = new SyncWrite().writeAndSync(request.getChannel(), request, 5000);
        //异步调用
        return response.getResult();
    }
}
