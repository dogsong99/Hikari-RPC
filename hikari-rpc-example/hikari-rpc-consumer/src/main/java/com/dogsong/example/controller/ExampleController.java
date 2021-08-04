package com.dogsong.example.controller;

import com.dogsong.example.ExampleService;
import com.dogsong.rpc.annotation.HikariRpcConsumer;
import org.springframework.web.bind.annotation.*;

/**
 * example consumer controller
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */

@RestController
@RequestMapping("/hikari-rpc/consumer")
public class ExampleController {

    @HikariRpcConsumer(providerName = "provider")
    private ExampleService exampleService;

    @PostMapping("/hello")
    public String helloWorld(@RequestParam String name) {
        return exampleService.helloWorld(name);
    }

    @GetMapping("sayHello")
    public String sayHello() {
        return exampleService.sayHello();
    }
}
