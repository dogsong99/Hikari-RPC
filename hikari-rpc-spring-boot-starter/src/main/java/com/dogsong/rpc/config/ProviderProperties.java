package com.dogsong.rpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 被调用方 配置信息
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
@Data
@ConfigurationProperties("hikari.rpc.provider")
public class ProviderProperties {

    /** 接口 */
    private String nozzle;

    /** 映射 */
    private String reference;

    /** 别名 */
    private String alias;

}
