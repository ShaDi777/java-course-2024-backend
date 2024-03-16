package edu.java.scrapper.jooq;

import edu.java.domain.jooq.dao.JooqLinkChatRepository;
import edu.java.domain.jooq.dao.JooqLinkRepository;
import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.domain.jooq.generated.tables.pojos.Link;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.assertj.core.data.TemporalUnitLessThanOffset;
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
public class JooqLinkServiceTest extends IntegrationTest {
    private static final String TEST_LINK = "https://github.com/ShaDi777/java-course-2024-backend";

    @Autowired private JooqLinkRepository linkRepository;
    @Autowired private JooqTgChatRepository chatRepository;
    @Autowired private JooqLinkChatRepository linkChatRepository;
    @Autowired private JooqTgChatService chatService;
    @Autowired private JooqLinkService linkService;

    @Test
    @Transactional
    @Rollback
    void registerNewLinkWithUnknownChatThrowsNotFound() {
        long unknownChatId = 1L;
        assertThrows(
            ResourceNotFoundException.chatNotFound(unknownChatId).getClass(),
            () -> linkService.add(unknownChatId, TEST_LINK)
        );
    }

    @Test
    @Transactional
    @Rollback
    void registerNewLinkWithExistingChat() {
        long chatId = 1L;
        chatService.register(chatId);

        linkService.add(chatId, TEST_LINK);

        assertThat(linkRepository.findByUrl(TEST_LINK)).isPresent();
        assertThat(linkChatRepository.findAllByChatId(chatId)).hasSize(1);
    }

    @Test
    @Transactional
    @Rollback
    void registerNewSameLinkInMultipleChats() {
        long chatId1 = 1L;
        long chatId2 = 2L;
        long chatId3 = 3L;
        chatService.register(chatId1);
        chatService.register(chatId2);
        chatService.register(chatId3);

        linkService.add(chatId1, TEST_LINK);
        linkService.add(chatId2, TEST_LINK);
        linkService.add(chatId3, TEST_LINK);

        assertThat(linkChatRepository.findAllByChatId(chatId1)).hasSize(1);
        assertThat(linkChatRepository.findAllByChatId(chatId2)).hasSize(1);
        assertThat(linkChatRepository.findAllByChatId(chatId3)).hasSize(1);
        assertThat(linkRepository.findByUrl(TEST_LINK)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    void removeNotTrackedLinkThrowsNotFound() {
        long chatId = 1L;
        assertThrows(
            ResourceNotFoundException.chatLinkNotFound(chatId, TEST_LINK).getClass(),
            () -> linkService.remove(chatId, TEST_LINK)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeTrackChatsAndDeleteLink() {
        long chatId1 = 1L;
        long chatId2 = 2L;
        chatService.register(chatId1);
        chatService.register(chatId2);
        linkService.add(chatId1, TEST_LINK);
        linkService.add(chatId2, TEST_LINK);

        linkService.remove(chatId1, TEST_LINK);
        assertThat(linkRepository.findByUrl(TEST_LINK)).isPresent();

        linkService.remove(chatId2, TEST_LINK);

        assertThat(linkChatRepository.findAllByChatId(chatId1)).isEmpty();
        assertThat(linkChatRepository.findAllByChatId(chatId2)).isEmpty();
        assertThat(linkRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void updateLastCheckedTimestamp() {
        long chatId = 1L;
        chatService.register(chatId);
        linkService.add(chatId, TEST_LINK);

        Link link = linkRepository.findByUrl(TEST_LINK).get();

        assertThat(link.getLastChecked()).isCloseTo(
            OffsetDateTime.ofInstant(Instant.EPOCH, ZoneId.of(ZoneOffset.UTC.getId())),
            new TemporalUnitLessThanOffset(24, ChronoUnit.HOURS)
        );

        OffsetDateTime newTime = OffsetDateTime.of(2024, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
        linkService.updateLastModified(link.getLinkId(), newTime);
        linkService.updateLastChecked(link.getLinkId(), newTime);

        Link updatedLink = linkRepository.findById(link.getLinkId()).get();
        assertThat(updatedLink).isNotNull();
        assertThat(updatedLink.getLastChecked()).isEqualTo(newTime);
        assertThat(updatedLink.getLastModified()).isEqualTo(newTime);
    }
}
