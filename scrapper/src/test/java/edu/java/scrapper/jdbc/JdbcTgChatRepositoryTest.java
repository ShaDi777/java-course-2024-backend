package edu.java.scrapper.jdbc;

import edu.java.dao.jdbc.JdbcLinkRepository;
import edu.java.dao.jdbc.JdbcTgChatRepository;
import edu.java.dao.model.Link;
import edu.java.dao.model.TgChat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class JdbcTgChatRepositoryTest extends IntegrationTest {
    private final JdbcTgChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;

    @Autowired JdbcTgChatRepositoryTest(JdbcTgChatRepository chatRepository, JdbcLinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void registerNewChat() {
        long chatId = 1L;
        chatRepository.createById(chatId);

        var chats = chatRepository.findAll();

        assertThat(chats).contains(TgChat.builder().chatId(chatId).build());
    }

    @Test
    @Transactional
    @Rollback
    void registerExistingChat() {
        long chatId = 1L;
        chatRepository.createById(chatId);

        assertThrows(ChatAlreadyExistsException.class, () -> chatRepository.createById(chatId));
    }

    @Test
    @Transactional
    @Rollback
    void unregisterExistingChat() {
        long chatId = 1L;
        chatRepository.createById(chatId);

        TgChat chat = chatRepository.deleteById(chatId);

        assertThat(chatRepository.findAll()).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void unregisterExistingChatAndRemoveTrackedLinks() {
        long chatId = 1L;
        String linkUrl = "https://github.com/ShaDi777/java-course-2024-backend";
        chatRepository.createById(chatId);
        Link link = linkRepository.add(chatId, linkUrl);

        TgChat chat = chatRepository.deleteById(chatId);

        assertThat(chatRepository.findAll()).doesNotContain(chat);
        assertThat(linkRepository.findAll()).doesNotContain(link);
        assertThat(linkRepository.findAllLinksByChatId(chatId)).doesNotContain(link);
    }

    @Test
    @Transactional
    @Rollback
    void findAllChats() {
        chatRepository.createById(1L);
        chatRepository.createById(2L);
        chatRepository.createById(3L);

        var chats = chatRepository.findAll();

        assertThat(chats)
            .contains(new TgChat(1L))
            .contains(new TgChat(2L))
            .contains(new TgChat(3L));
    }
}
