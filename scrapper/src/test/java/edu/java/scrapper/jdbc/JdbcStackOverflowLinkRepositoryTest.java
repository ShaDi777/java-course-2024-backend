package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.dao.JdbcStackOverflowLinkRepository;
import edu.java.domain.jdbc.dao.JdbcTgChatRepository;
import edu.java.domain.jdbc.model.StackOverflowLink;
import edu.java.domain.jdbc.model.TgChat;
import edu.java.dto.link.LinkInfoDto;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jdbc.JdbcLinkService;
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

    @Autowired private JdbcTgChatRepository chatRepository;
    @Autowired private JdbcStackOverflowLinkRepository stackOverflowLinkRepository;
    @Autowired private JdbcLinkService linkService;

    @Test
    @Transactional
    @Rollback
    void upsertNewLink() {
        long chatId = 1L;
        chatRepository.save(new TgChat(chatId));
        LinkInfoDto link = linkService.add(chatId, TEST_URL);

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
        long chatId = 1L;
        chatRepository.save(new TgChat(chatId));
        LinkInfoDto link = linkService.add(chatId, TEST_URL);

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
