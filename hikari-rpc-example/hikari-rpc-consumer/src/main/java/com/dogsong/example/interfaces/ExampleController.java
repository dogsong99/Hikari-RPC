// package com.dogsong.example.interfaces;
//
// import com.dogsong.example.ExampleService;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
//
// import javax.annotation.Resource;
//
// /**
//  * TODO
//  *
//  * @author <a href="mailto:domi.song@yunzhihui.com">domisong</a>
//  * @since 2021/7/29
//  */
//
// @RestController("/hikari-rpc/consumer")
// public class ExampleController {
//
//
//     @Resource
//     private ExampleService exampleService;
//
//     @PostMapping ("/hello")
//     public String helloWorld(@RequestBody String str) {
//         String helloWorld = exampleService.helloWorld(str);
//         return helloWorld;
//     }
// }
