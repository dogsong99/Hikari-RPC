package com.dogsong.example.controller;

import com.dogsong.example.ExampleService;
import com.dogsong.rpc.annotation.HikariRpcConsumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */

@RestController
@RequestMapping("/hikari-rpc/consumer")
public class ExampleController {

    @HikariRpcConsumer(providerName = "provider")
    private ExampleService exampleService;

    @GetMapping("/hello")
    public String helloWorld() {
        return exampleService.helloWorld("11111");
    }
}
