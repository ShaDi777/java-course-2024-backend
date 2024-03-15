package edu.java.scrapper.jdbc;

import edu.java.dao.jdbc.JdbcLinkRepository;
import edu.java.dao.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.dao.jdbc.JdbcTgChatRepository;
import edu.java.dao.model.Link;
import edu.java.dao.model.StackOverflowLink;
import edu.java.dao.model.TgChat;
import edu.java.scrapper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class JdbcStackOverflowLinkRepositoryTest extends IntegrationTest {
    public static final String TEST_URL =
        "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array";
    private final JdbcTgChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Autowired JdbcStackOverflowLinkRepositoryTest(
        JdbcTgChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcStackOverflowLinkRepository stackOverflowLinkRepository
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.stackOverflowLinkRepository = stackOverflowLinkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void upsertNewLink() {
        Long chatId = 1L;

        TgChat chat = chatRepository.createById(chatId);
        Link link = linkRepository.add(chatId, TEST_URL);

        StackOverflowLink stackOverflowLink = StackOverflowLink.builder()
            .linkId(link.getLinkId())
            .isAnswered(true)
            .answersCount(1)
            .commentsCount(1)
            .build();

        stackOverflowLinkRepository.upsertLink(stackOverflowLink);
        Optional<StackOverflowLink> stackOverflowLinkFromRepo = stackOverflowLinkRepository.findById(link.getLinkId());

        assertThat(stackOverflowLinkFromRepo.isPresent()).isTrue();
        assertThat(stackOverflowLinkFromRepo.get()).isEqualTo(stackOverflowLink);
    }

    @Test
    @Transactional
    @Rollback
    void upsertExistingLink() {
        Long chatId = 1L;
        TgChat chat = chatRepository.createById(chatId);
        Link link = linkRepository.add(chatId, TEST_URL);

        StackOverflowLink oldStackOverflowLink = StackOverflowLink.builder()
            .linkId(link.getLinkId())
            .isAnswered(false)
            .answersCount(0)
            .commentsCount(0)
            .build();
        stackOverflowLinkRepository.upsertLink(oldStackOverflowLink);

        StackOverflowLink newStackOverflowLink = StackOverflowLink.builder()
            .linkId(link.getLinkId())
            .isAnswered(true)
            .answersCount(1)
            .commentsCount(1)
            .build();
        stackOverflowLinkRepository.upsertLink(newStackOverflowLink);

        Optional<StackOverflowLink> stackOverflowLinkFromRepo = stackOverflowLinkRepository.findById(link.getLinkId());

        assertThat(stackOverflowLinkFromRepo.isPresent()).isTrue();
        assertThat(stackOverflowLinkFromRepo.get()).isEqualTo(newStackOverflowLink);
    }

    @Test
    void findNonExistingLink() {
        Optional<StackOverflowLink> stackOverflowLinkFromRepo = stackOverflowLinkRepository.findById(1L);
        assertThat(stackOverflowLinkFromRepo.isEmpty()).isTrue();
    }
}
