package edu.java.exceptions;

public class ChatNotFoundException extends RuntimeException {
    private ChatNotFoundException(String message) {
        super(message);
    }

    public ChatNotFoundException(long tgChatId) {
        this("Chat " + tgChatId + " not found.");
    }
}
