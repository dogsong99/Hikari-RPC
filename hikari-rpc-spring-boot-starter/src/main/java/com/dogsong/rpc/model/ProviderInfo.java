package com.dogsong.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 注册服务信息实体
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProviderInfo {

    /** provider name */
    private String name;

    /** provider address */
    private String addr;

    public ProviderInfo setName(String name) {
        this.name = name;
        return this;
    }

    public ProviderInfo setAddr(String addr) {
        this.addr = addr;
        return this;
    }

}
