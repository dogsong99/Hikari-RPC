package com.dogsong.rpc.annotation;

import com.dogsong.rpc.config.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 rpc 服务
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ServerAutoConfiguration.class, ProviderAutoConfiguration.class, ConsumerFactoryBean.class})
@EnableConfigurationProperties({ServerProperties.class, ProviderProperties.class, ConsumerProperties.class})
@ComponentScan("com.dogsong.*")
public @interface EnableHikariRpc {

}
