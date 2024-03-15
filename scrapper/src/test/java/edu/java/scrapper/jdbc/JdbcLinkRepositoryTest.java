package edu.java.scrapper.jdbc;

import edu.java.dao.jdbc.JdbcLinkRepository;
import edu.java.dao.jdbc.JdbcTgChatRepository;
import edu.java.dao.model.Link;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.scrapper.IntegrationTest;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {
    private static final String TEST_LINK = "https://github.com/ShaDi777/java-course-2024-backend";

    private final JdbcTgChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;

    @Autowired JdbcLinkRepositoryTest(JdbcTgChatRepository chatRepository, JdbcLinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void registerNewLinkWithUnknownChatThrowsNotFound() {
        long unknownChatId = 1L;
        assertThrows(
            ResourceNotFoundException.chatNotFound(unknownChatId).getClass(),
            () -> linkRepository.add(unknownChatId, TEST_LINK)
        );
    }

    @Test
    @Transactional
    @Rollback
    void registerNewLinkWithExistingChat() {
        long chatId = 1L;
        chatRepository.createById(chatId);

        Link link = linkRepository.add(chatId, TEST_LINK);

        assertThat(linkRepository.findAll()).contains(link);
        assertThat(linkRepository.findAllLinksByChatId(chatId)).contains(link);
    }

    @Test
    @Transactional
    @Rollback
    void registerNewSameLinkInMultipleChats() {
        long chatId1 = 1L;
        long chatId2 = 2L;
        long chatId3 = 3L;
        chatRepository.createById(chatId1);
        chatRepository.createById(chatId2);
        chatRepository.createById(chatId3);

        Link link1 = linkRepository.add(chatId1, TEST_LINK);
        Link link2 = linkRepository.add(chatId2, TEST_LINK);
        Link link3 = linkRepository.add(chatId3, TEST_LINK);

        assertThat(link1).isEqualTo(link2).isEqualTo(link3);
        assertThat(linkRepository.findAllLinksByChatId(chatId1)).contains(link1);
        assertThat(linkRepository.findAllLinksByChatId(chatId2)).contains(link1);
        assertThat(linkRepository.findAllLinksByChatId(chatId3)).contains(link1);
        assertThat(linkRepository.findAll()).containsOnlyOnce(link1);
    }

    @Test
    @Transactional
    @Rollback
    void removeNotTrackedLinkThrowsNotFound() {
        long chatId = 1L;
        assertThrows(
            ResourceNotFoundException.linkNotFound(chatId, TEST_LINK).getClass(),
            () -> linkRepository.remove(chatId, TEST_LINK)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeNotTrackedByChatLinkThrowsNotFound() {
        long trackingChatId = 1L;
        long untrackingChatId = 2L;
        chatRepository.createById(trackingChatId);
        chatRepository.createById(untrackingChatId);
        Link link = linkRepository.add(trackingChatId, TEST_LINK);

        assertThat(linkRepository.findAllLinksByChatId(untrackingChatId)).isEmpty();
        assertThrows(
            ResourceNotFoundException.linkNotFound(untrackingChatId, TEST_LINK).getClass(),
            () -> linkRepository.remove(untrackingChatId, TEST_LINK)
        );
    }

    @Test
    @Transactional
    @Rollback
    void removeTrackChatsAndDeleteLink() {
        long chatId1 = 1L;
        long chatId2 = 2L;
        chatRepository.createById(chatId1);
        chatRepository.createById(chatId2);
        Link link1 = linkRepository.add(chatId1, TEST_LINK);
        Link link2 = linkRepository.add(chatId2, TEST_LINK);

        linkRepository.remove(chatId1, TEST_LINK);
        assertThat(linkRepository.findAll()).contains(link1);

        Link removedLink = linkRepository.remove(chatId2, TEST_LINK);
        assertThat(linkRepository.findAllLinksByChatId(chatId1)).doesNotContain(removedLink);
        assertThat(linkRepository.findAllLinksByChatId(chatId2)).doesNotContain(removedLink);
        assertThat(linkRepository.findAll()).doesNotContain(removedLink);
    }

    @Test
    @Transactional
    @Rollback
    void updateLastModifiedTimestamp() {
        long chatId = 1L;
        chatRepository.createById(chatId);
        Link link = linkRepository.add(chatId, TEST_LINK);

        assertThat(link.getLastModified()).isCloseToUtcNow(new TemporalUnitLessThanOffset(30, ChronoUnit.SECONDS));

        OffsetDateTime newUpdateTime = OffsetDateTime.of(2024, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
        linkRepository.updateLastModified(link.getLinkId(), newUpdateTime);

        Link updatedLink = linkRepository.findById(link.getLinkId());
        assertThat(updatedLink).isNotNull();
        assertThat(updatedLink.getLastModified()).isEqualTo(newUpdateTime);
    }

    @Test
    @Transactional
    @Rollback
    void updateLastCheckedTimestamp() {
        long chatId = 1L;
        chatRepository.createById(chatId);
        Link link = linkRepository.add(chatId, TEST_LINK);

        assertThat(link.getLastChecked()).isCloseTo(
            OffsetDateTime.ofInstant(Instant.EPOCH, ZoneId.of(ZoneOffset.UTC.getId())),
            new TemporalUnitLessThanOffset(24, ChronoUnit.HOURS)
        );

        OffsetDateTime newCheckTime = OffsetDateTime.of(2024, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
        linkRepository.updateLastChecked(link.getLinkId(), newCheckTime);

        Link updatedLink = linkRepository.findById(link.getLinkId());
        assertThat(updatedLink).isNotNull();
        assertThat(updatedLink.getLastChecked()).isEqualTo(newCheckTime);
    }
}
