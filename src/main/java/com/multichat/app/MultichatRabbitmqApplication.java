package com.multichat.app;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MultichatRabbitmqApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultichatRabbitmqApplication.class, args);
    }
}
