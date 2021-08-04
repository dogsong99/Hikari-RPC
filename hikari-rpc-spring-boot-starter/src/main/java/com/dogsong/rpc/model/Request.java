package com.dogsong.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * hikari-rpc server request
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {

    /** 请求ID */
    private String requestId;

    /** 类名 */
    private String className;

    /** 方法名 */
    private String methodName;

    /** 参数类型 */
    private Class<?>[] paramTypes;

    /** 参数 */
    private Object[] params;


    public Request setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Request setClassName(String className) {
        this.className = className;
        return this;
    }

    public Request setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Request setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
        return this;
    }

    public Request setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
