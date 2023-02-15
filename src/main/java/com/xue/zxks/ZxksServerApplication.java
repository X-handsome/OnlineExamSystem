package com.xue.zxks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ZxksServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZxksServerApplication.class, args);
    }
}
