package com.dogsong.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * hikari-rpc server response
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {

    /** 请求ID */
    private String requestId;

    /** error info */
    private Throwable error;

    /** 结果 */
    private Object result;


    public boolean isError() {
        return error != null;
    }

}
