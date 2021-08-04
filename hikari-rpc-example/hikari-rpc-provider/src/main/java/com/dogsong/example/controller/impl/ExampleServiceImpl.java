package com.dogsong.example.controller.impl;

import com.dogsong.example.ExampleService;
import com.dogsong.rpc.annotation.HikariRpcService;

/**
 * example service
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
@HikariRpcService(ExampleService.class)
public class ExampleServiceImpl implements ExampleService {

    @Override
    public String helloWorld(String str) {
        return "Hello, " + str;
    }

    @Override
    public String sayHello() {
        return "Hello, Hikari-RPC";
    }

}
