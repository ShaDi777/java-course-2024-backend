package edu.java.scrapper.jpa;

import edu.java.domain.jpa.dao.JpaTgChatRepository;
import edu.java.domain.jpa.model.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jpa.JpaLinkChatService;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "app.database-access-type=jpa")
@Testcontainers
public class JpaTgChatServiceTest extends IntegrationTest {
    @Autowired private JpaTgChatRepository chatRepository;
    @Autowired private JpaTgChatService chatService;
    @Autowired private JpaLinkService linkService;
    @Autowired private JpaLinkChatService linkChatService;

    @Test
    @Transactional
    @Rollback
    void registerNewChat() {
        long chatId = 1L;
        chatService.register(chatId);

        var chats = chatRepository.findAll();

        assertThat(chats).anyMatch(x -> x.getChatId().equals(chatId));
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

        assertThat(chatRepository.findAll()).doesNotContain(new Chat(chatId, new ArrayList<>()));
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
        assertThat(linkChatService.listAllLinksByChatId(chatId)).isEmpty();
    }
}
