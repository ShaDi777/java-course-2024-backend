package edu.java.bot.linktracker.chats;

public interface ChatRepository {
    void registerChat(long chatId);

    void unregisterChat(long chatId);
}
