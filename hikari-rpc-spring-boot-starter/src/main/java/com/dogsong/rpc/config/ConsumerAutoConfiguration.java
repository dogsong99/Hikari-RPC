package com.dogsong.rpc.config;

import com.dogsong.rpc.annotation.HikariRpcConsumer;
import com.dogsong.rpc.client.HikariProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * 服务消费者
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
@Configuration
@ConditionalOnClass(HikariRpcConsumer.class)
@EnableConfigurationProperties(HikariProperties.class)
public class ConsumerAutoConfiguration {

    @Autowired
    private HikariProxy hikariProxy;

    /**
     * 设置代理
     */
    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName)
                    throws BeansException {
                Class<?> objClz = bean.getClass();
                // 过滤所有声明的属性
                for (Field declaredField : objClz.getDeclaredFields()) {
                    // 查找属性上标注 @HikariRpcConsumer 注解的
                    HikariRpcConsumer annotation = declaredField.getAnnotation(HikariRpcConsumer.class);
                    if (null != annotation) {
                        Class<?> type = declaredField.getType();
                        // 设置字段属性为可见
                        declaredField.setAccessible(true);
                        try {
                            declaredField.set(bean, hikariProxy.create(type, annotation.providerName()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } finally {
                            // 回滚
                            declaredField.setAccessible(false);
                        }
                    }
                }
                return bean;
            }
        };
    }
}
