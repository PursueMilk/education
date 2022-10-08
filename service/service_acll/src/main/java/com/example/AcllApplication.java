package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.mapper")
@ComponentScan({"com.example","com.atguigu"})
public class AcllApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcllApplication.class,args);
    }
}
