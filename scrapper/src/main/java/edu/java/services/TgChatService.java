package edu.java.services;

import edu.java.dao.model.TgChat;

public interface TgChatService {
    TgChat register(long tgChatId);

    TgChat unregister(long tgChatId);
}
