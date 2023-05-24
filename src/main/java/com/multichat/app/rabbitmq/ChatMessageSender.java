package com.multichat.app.rabbitmq;

import com.multichat.app.SenderUsernameHolder;
import com.multichat.app.entity.ChatMessage;
import com.multichat.app.entity.ChatRecipientType;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ChatMessageSender {
    private final SenderUsernameHolder senderUsernameHolder;
    private final AmqpTemplate rabbitTemplate;

    @Value("${chat.rabbitmq.fanout-exchange}")
    private String fanoutExchange;

    @Value("${chat.rabbitmq.direct-exchange}")
    private String directExchange;

    @Value("${chat.rabbitmq.routing-key}")
    private String routingKey;

    @Autowired
    public ChatMessageSender(SenderUsernameHolder senderUsernameHolder, AmqpTemplate rabbitTemplate) {
        this.senderUsernameHolder = senderUsernameHolder;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ChatMessage chatMessage, String routingKey, String exchange) {
        rabbitTemplate.convertAndSend(exchange, routingKey, chatMessage);
        System.out.println("Sent message: " + chatMessage.getContent() + " to " + chatMessage.getRecipient());
        System.out.println("-------------------");
    }

    public void sendChatMessageToUser(String sender, String recipient) {
        System.out.print("Enter your message: ");
        String content = new Scanner(System.in).nextLine();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        chatMessage.setChatRecipientType(ChatRecipientType.USER);
        chatMessage.setContent(content);

        sendMessage(chatMessage, this.routingKey + recipient, this.directExchange + recipient);
    }

    public void sendChatMessageToChatRoom(String sender, String chatRoomName) {
        System.out.print("Enter your message: ");
        String content = new Scanner(System.in).nextLine();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setRecipient(chatRoomName);
        chatMessage.setChatRecipientType(ChatRecipientType.CHAT_ROOM);
        chatMessage.setContent(content);

        sendMessage(chatMessage, "", this.fanoutExchange);
    }

    public void startReadingCommands() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------\nAvailable commands:\n 1. new-message - send a message\n 2. join-chatroom <chatroom> - join a chat room\n 3. leave-chatroom <chatroom> - leave a chat room\n-------------------\n");

        while (true) {
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("new-message")) {
                startSendingMessage(scanner);
            } else if (command.toLowerCase().startsWith("join-chatroom")) {
                System.out.print("Enter the chat room name: ");
                String chatRoomName = scanner.nextLine();
                senderUsernameHolder.addChatRoom(chatRoomName);
                System.out.println(" (!) Joined a new chat room: " + chatRoomName);
            } else if (command.toLowerCase().startsWith("leave-chatroom")) {
                System.out.print("Enter the chat room name: ");
                String chatRoomName = scanner.nextLine();
                senderUsernameHolder.addChatRoom(chatRoomName);
                System.out.println(" (!) Left the chat room: " + chatRoomName);
            }
        }
    }

    public void startSendingMessage(Scanner scanner) {
        System.out.print("Select a recipient (user/room), or enter 'exit' to quit: ");
        String recipientType = scanner.nextLine();

        if (recipientType.equalsIgnoreCase("user")) {
            System.out.print("Enter the recipient's username: ");
            String recipient = scanner.nextLine();
            sendChatMessageToUser(senderUsernameHolder.getSenderUsername(), recipient);
        } else if (recipientType.equalsIgnoreCase("room")) {
            System.out.print("Enter the chat room name: ");
            String chatRoomName = scanner.nextLine();
            if (!senderUsernameHolder.getChatRooms().contains(chatRoomName)) {
                System.out.println(" (!) You haven't joined this room yet!");
                return;
            }
            sendChatMessageToChatRoom(senderUsernameHolder.getSenderUsername(), chatRoomName);
        } else if (recipientType.equalsIgnoreCase("exit")) {
            System.out.println(" (!) Canceled message sending!");
        } else {
            System.out.println(" (!) Invalid argument. Should be 'user' or 'room'!");
        }
    }
}
