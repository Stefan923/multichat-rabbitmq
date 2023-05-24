package com.multichat.app.rabbitmq;

import com.multichat.app.SenderUsernameHolder;
import com.multichat.app.entity.ChatMessage;
import com.multichat.app.entity.ChatRecipientType;
import com.multichat.app.entity.ChatRoom;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatMessageReceiver {
    private final String username;
    private final SenderUsernameHolder senderUsernameHolder;
    private final List<ChatRoom> chatRooms = new ArrayList<>();

    @Autowired
    public ChatMessageReceiver(SenderUsernameHolder senderUsernameHolder) {
        this.username = senderUsernameHolder.getSenderUsername();
        this.senderUsernameHolder = senderUsernameHolder;
    }

    @RabbitListener(queues = {"${chat.rabbitmq.queue}" + "#{@chatUsername}" + "f", "${chat.rabbitmq.queue}" + "#{@chatUsername}" + "d"})
    public void receiveMessage(ChatMessage chatMessage) {
        if (chatMessage.getChatRecipientType().equals(ChatRecipientType.USER)) {
            handleOneToOneMessage(chatMessage);
        } else {
            handleChatRoomMessage(chatMessage);
        }
    }

    private void handleOneToOneMessage(ChatMessage chatMessage) {
        if (chatMessage.getRecipient().equals(senderUsernameHolder.getSenderUsername())) {
            System.out.println("Private Message from " + chatMessage.getSender() + ": " + chatMessage.getContent());
            System.out.println("-------------------");
        }
    }

    private void handleChatRoomMessage(ChatMessage chatMessage) {
        String sender = chatMessage.getSender();
        String chatRoomName = chatMessage.getRecipient();

        if (senderUsernameHolder.getChatRooms().contains(chatRoomName)) {
            System.out.println("Chat Room: " + chatRoomName);
            System.out.println("Sender: " + sender);
            System.out.println("Message: " + chatMessage.getContent());
            System.out.println("-------------------");
        }

        ChatRoom chatRoom = findChatRoom(chatRoomName);
        if (chatRoom != null) {
            chatRoom.addMessage(chatMessage);
        }
    }

    private ChatRoom findChatRoom(String chatRoomName) {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getName().equals(chatRoomName)) {
                return chatRoom;
            }
        }
        return null;
    }
}

