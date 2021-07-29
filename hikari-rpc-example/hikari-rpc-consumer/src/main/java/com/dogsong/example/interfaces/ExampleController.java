package com.dogsong.example.interfaces;

import com.dogsong.example.ExampleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/29
 */

@RestController("/hikari-rpc/consumer")
public class ExampleController {

    private ExampleService exampleService;

    @GetMapping("/hello")
    public String helloWorld() {
        String helloWorld = exampleService.helloWorld("11111");
        return helloWorld;
    }
}
