package com.syntiaro_pos_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class SyntiaroPosSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SyntiaroPosSystemApplication.class, args);
    }

}
