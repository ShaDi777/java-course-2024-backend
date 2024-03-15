package edu.java.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    protected ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException chatNotFound(long tgChatId) {
        return new ResourceNotFoundException(String.format("Chat %d not found.", tgChatId));
    }

    public static ResourceNotFoundException linkNotFound(long tgChatId, String url) {
        return new ResourceNotFoundException(String.format("In chat %d link %s not found", tgChatId, url));
    }
}
