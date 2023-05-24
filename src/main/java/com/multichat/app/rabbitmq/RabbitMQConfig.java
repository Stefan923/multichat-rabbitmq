package com.multichat.app.rabbitmq;

import com.multichat.app.SenderUsernameHolder;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class RabbitMQConfig {
    private String username;

    @Autowired
    public RabbitMQConfig(SenderUsernameHolder senderUsernameHolder) {
        System.out.print("Enter your username: ");
        this.username = new Scanner(System.in).nextLine();
        senderUsernameHolder.setSenderUsername(username);
    }

    @Bean
    public Queue fanoutChatQueue() {
        return new Queue("multichat_queue" + username + "f", true);
    }

    @Bean
    public Queue directChatQueue() {
        return new Queue("multichat_queue" + username + "d", true);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange-m");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange" + username);
    }

    @Bean
    public Binding fanoutBinding(Queue fanoutChatQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutChatQueue).to(fanoutExchange);
    }

    @Bean
    public Binding directBinding(Queue directChatQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(directChatQueue).to(directExchange).with("multichat_routing_key" + username);
    }

    @Bean
    public String chatUsername() {
        return this.username;
    }
}
