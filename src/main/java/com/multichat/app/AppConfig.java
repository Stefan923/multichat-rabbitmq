package com.multichat.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public SenderUsernameHolder senderUsernameHolder() {
        return new SenderUsernameHolder();
    }
}
