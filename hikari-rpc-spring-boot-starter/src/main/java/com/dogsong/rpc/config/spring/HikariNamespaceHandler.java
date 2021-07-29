package com.dogsong.rpc.config.spring;

import com.dogsong.rpc.config.spring.bean.ConsumerBean;
import com.dogsong.rpc.config.spring.bean.ProviderBean;
import com.dogsong.rpc.config.spring.bean.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * TODO
 * @author <a href="mailto:domi.song@cloudwise.com">domi</a>
 * @since 2021/7/29.
 */
public class HikariNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("server", new HikariBeanDefinitionParser(ServerBean.class));
        registerBeanDefinitionParser("provider", new HikariBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("consumer", new HikariBeanDefinitionParser(ConsumerBean.class));
    }

}
