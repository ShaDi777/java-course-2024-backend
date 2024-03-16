package edu.java.bot.linktracker.chats;

import edu.java.bot.client.ScrapperTgChatsHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapperChatRepository implements ChatRepository {
    private final ScrapperTgChatsHttpClient chatsHttpClient;

    @Override
    public void registerChat(long chatId) {
        chatsHttpClient.addTgChat(chatId);
    }

    @Override
    public void unregisterChat(long chatId) {
        chatsHttpClient.deleteTgChat(chatId);
    }
}
