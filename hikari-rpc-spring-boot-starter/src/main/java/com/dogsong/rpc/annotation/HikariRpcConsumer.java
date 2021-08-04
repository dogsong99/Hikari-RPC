package com.dogsong.rpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * hikari-rpc 服务调用方
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface HikariRpcConsumer {

    /** 服务名称 */
    String providerName();

}
