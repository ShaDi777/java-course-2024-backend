package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.dao.JdbcTgChatRepository;
import edu.java.domain.jdbc.model.TgChat;
import edu.java.scrapper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties="app.database-access-type=jdbc")
@Testcontainers
public class JdbcTgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTgChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addNewChat() {
        TgChat chat = new TgChat(1L);

        chatRepository.save(chat);

        var chats = chatRepository.findAll();
        assertThat(chats).contains(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteExistingChat() {
        long chatId = 1L;
        TgChat chat = new TgChat(chatId);

        chatRepository.save(chat);
        chatRepository.deleteById(chatId);

        var chats = chatRepository.findAll();
        assertThat(chats).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteNonExistingChat() {
        long chatId = 1L;
        TgChat chat = new TgChat(chatId);

        chatRepository.deleteById(chatId);

        var chats = chatRepository.findAll();
        assertThat(chats).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdExist() {
        long chatId = 1L;
        TgChat chat = new TgChat(chatId);

        chatRepository.save(chat);

        Optional<TgChat> foundChat = chatRepository.findById(chatId);
        assertThat(foundChat).isPresent();
        assertThat(foundChat.get()).isEqualTo(chat);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdNotExist() {
        long chatId = 1L;

        Optional<TgChat> foundChat = chatRepository.findById(chatId);
        assertThat(foundChat).isEmpty();
    }
}
