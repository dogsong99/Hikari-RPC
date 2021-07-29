package com.dogsong.example;

import com.dogsong.rpc.annotation.EnableHikariRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO
 *
 * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
 * @since 2021/7/29
 */
@SpringBootApplication
@EnableHikariRpc
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
