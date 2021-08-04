package com.dogsong.rpc.server;

import com.dogsong.rpc.model.Request;
import com.dogsong.rpc.model.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 服务 Response 处理
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        logger.info("provider accept request,{}", request);
        Response response = new Response();
        // 将请求id原路带回
        response.setRequestId(request.getRequestId());
        try {
            Object result = handler(request);
            response.setResult(result);
        } catch (Exception e) {
            response.setError(e);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handler(Request request) throws Exception {
        String className = request.getClassName();
        // 获取声明的类
        Class<?> objClz = Class.forName(className);
        Object bean = BeanFactory.getBean(objClz);
        // 获取调用的方法名称
        String methodName = request.getMethodName();
        // 参数类型
        Class<?>[] paramTypes = request.getParamTypes();
        // 具体参数
        Object[] params = request.getParams();
        // 调用实现类的指定的方法并返回结果
        Method method = objClz.getMethod(methodName, paramTypes);
        return method.invoke(bean, params);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("netty provider server caught error,", cause);
        ctx.close();
    }


}
