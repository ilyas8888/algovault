package com.algovault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AlgovaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgovaultApplication.class, args);
    }

}
