package com.multichat.app;

import java.util.ArrayList;
import java.util.List;

public class SenderUsernameHolder {
    private String senderUsername = "unknown";
    private List<String> chatRooms = new ArrayList<>();

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public List<String> getChatRooms() {
        return chatRooms;
    }

    public void addChatRoom(String chatRoom) {
        chatRooms.add(chatRoom);
    }

    public void removeChatRoom(String chatRoom) {
        chatRooms.remove(chatRoom);
    }
}
