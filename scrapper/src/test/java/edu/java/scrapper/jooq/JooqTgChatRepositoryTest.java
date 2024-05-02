package edu.java.scrapper.jooq;

import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.domain.jooq.generated.tables.pojos.Chat;
import edu.java.scrapper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties="app.database-access-type=jooq")
@Testcontainers
public class JooqTgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqTgChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addNewChat() {
        Chat chat = new Chat(1L);

        chatRepository.add(chat);

        var chats = chatRepository.getAll();
        assertThat(chats).contains(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteExistingChat() {
        long chatId = 1L;
        Chat chat = new Chat(chatId);

        chatRepository.add(chat);
        chatRepository.deleteById(chatId);

        var chats = chatRepository.getAll();
        assertThat(chats).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteNonExistingChat() {
        long chatId = 1L;
        Chat chat = new Chat(chatId);

        chatRepository.deleteById(chatId);

        var chats = chatRepository.getAll();
        assertThat(chats).doesNotContain(chat);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdExist() {
        long chatId = 1L;
        Chat chat = new Chat(chatId);

        chatRepository.add(chat);

        Optional<Chat> foundChat = chatRepository.getById(chatId);
        assertThat(foundChat).isPresent();
        assertThat(foundChat.get()).isEqualTo(chat);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdNotExist() {
        long chatId = 1L;

        Optional<Chat> foundChat = chatRepository.getById(chatId);
        assertThat(foundChat).isEmpty();
    }
}
