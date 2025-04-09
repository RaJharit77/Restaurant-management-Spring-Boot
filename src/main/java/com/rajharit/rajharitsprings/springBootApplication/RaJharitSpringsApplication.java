package com.rajharit.rajharitsprings.springBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.rajharit.rajharitsprings.springBootApplication", "helloWorld", "com.rajharit.rajharitsprings"})
public class RaJharitSpringsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RaJharitSpringsApplication.class, args);
    }

}
