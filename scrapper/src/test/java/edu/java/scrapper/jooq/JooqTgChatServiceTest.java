package edu.java.scrapper.jooq;

import edu.java.domain.jooq.dao.JooqLinkChatRepository;
import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.domain.jooq.generated.tables.pojos.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties="app.database-access-type=jooq")
@Testcontainers
public class JooqTgChatServiceTest extends IntegrationTest {
    @Autowired private JooqTgChatRepository chatRepository;
    @Autowired private JooqLinkChatRepository linkChatRepository;
    @Autowired private JooqTgChatService chatService;
    @Autowired private JooqLinkService linkService;

    @Test
    @Transactional
    @Rollback
    void registerNewChat() {
        long chatId = 1L;
        chatService.register(chatId);

        var chats = chatRepository.getAll();

        assertThat(chats).contains(new Chat(chatId));
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

        assertThat(chatRepository.getAll()).doesNotContain(new Chat(chatId));
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

        assertThat(chatRepository.getAll()).isEmpty();
        assertThat(linkChatRepository.findAllByChatId(chatId)).isEmpty();
    }
}
