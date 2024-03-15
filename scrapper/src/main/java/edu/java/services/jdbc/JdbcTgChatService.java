package edu.java.services.jdbc;

import edu.java.dao.jdbc.JdbcTgChatRepository;
import edu.java.dao.model.TgChat;
import edu.java.services.TgChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository chatRepository;

    @Override
    public TgChat register(long tgChatId) {
        return chatRepository.createById(tgChatId);
    }

    @Override
    public TgChat unregister(long tgChatId) {
        return chatRepository.deleteById(tgChatId);
    }
}
