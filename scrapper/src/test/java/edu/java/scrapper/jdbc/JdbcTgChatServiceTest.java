package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.dao.JdbcLinkChatRepository;
import edu.java.domain.jdbc.dao.JdbcTgChatRepository;
import edu.java.domain.jdbc.model.TgChat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties="app.database-access-type=jdbc")
@Testcontainers
public class JdbcTgChatServiceTest extends IntegrationTest {
    @Autowired private JdbcTgChatRepository chatRepository;
    @Autowired private JdbcLinkChatRepository linkChatRepository;
    @Autowired private JdbcTgChatService chatService;
    @Autowired private JdbcLinkService linkService;

    @Test
    @Transactional
    @Rollback
    void registerNewChat() {
        long chatId = 1L;
        chatService.register(chatId);

        var chats = chatRepository.findAll();

        assertThat(chats).contains(TgChat.builder().chatId(chatId).build());
    }

    @Test
    @Transactional
    @Rollback
    void registerExistingChat() {
        long chatId = 1L;
        chatService.register(chatId);

        assertThrows(ChatAlreadyExistsException.class, () -> chatService.register(chatId));
    }

    @Test
    @Transactional
    @Rollback
    void unregisterExistingChat() {
        long chatId = 1L;
        chatService.register(chatId);
        chatService.unregister(chatId);

        assertThat(chatRepository.findAll()).doesNotContain(TgChat.builder().chatId(chatId).build());
    }

    @Test
    @Transactional
    @Rollback
    void unregisterExistingChatAndRemoveTrackedLinks() {
        long chatId = 1L;
        String linkUrl = "https://github.com/ShaDi777/java-course-2024-backend";
        chatService.register(chatId);
        linkService.add(chatId, linkUrl);

        chatService.unregister(chatId);

        assertThat(chatRepository.findAll()).isEmpty();
        assertThat(linkChatRepository.findAllLinksByChatId(chatId)).isEmpty();
    }
}
