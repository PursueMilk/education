package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
//注册Nacos
@EnableDiscoveryClient
//开启远程调用
@EnableFeignClients
public class EduMain {
    public static void main(String[] args) {
        SpringApplication.run(EduMain.class, args);
    }
}
