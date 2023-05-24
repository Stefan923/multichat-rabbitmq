package com.multichat.app;

import com.multichat.app.rabbitmq.ChatMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ChatAppRunner implements ApplicationRunner {
    private final ChatMessageSender chatMessageSender;

    @Autowired
    public ChatAppRunner(ChatMessageSender chatMessageSender) {
        this.chatMessageSender = chatMessageSender;
    }

    @Override
    public void run(ApplicationArguments args) {
        chatMessageSender.startReadingCommands();
    }
}
