package com.dogsong.example.interfaces;

import com.dogsong.example.ExampleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/29
 */
@Controller("exampleService")
public class ExampleServiceImpl implements ExampleService {

    @Override
    public String helloWorld(String str) {
        return "Hello, Hikari-RPC" + str;
    }

}
