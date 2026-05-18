package oopproject.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import oopproject.users.User;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int senderId;
    private final String senderUsername;
    private final int recipientId;
    private final String recipientUsername;
    private final String content;
    private final LocalDateTime sentAt;

    public Message(User sender, User recipient, String content) {
        this.senderId = sender.getId();
        this.senderUsername = sender.getUsername();
        this.recipientId = recipient.getId();
        this.recipientUsername = recipient.getUsername();
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        return sentAt + " " + senderUsername + " -> " + recipientUsername + ": " + content;
    }
}
