package com.mf.plock.springboot.starter.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PlockTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlockTestApplication.class, args);
    }
}
