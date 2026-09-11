package com.curiousape.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        System.out.println("🐵 The Curious Ape Blog (Spring Boot) is running at http://localhost:8080");
    }
}
