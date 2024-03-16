package edu.java.scrapper.jooq;

import edu.java.domain.jooq.dao.JooqStackOverflowRepository;
import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.domain.jooq.generated.tables.pojos.Chat;
import edu.java.domain.jooq.generated.tables.pojos.LinkStackoverflow;
import edu.java.dto.link.LinkInfoDto;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jooq.JooqLinkService;
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
public class JooqStackOverflowLinkRepositoryTest extends IntegrationTest {
    public static final String TEST_URL =
        "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array";

    @Autowired private JooqTgChatRepository chatRepository;
    @Autowired private JooqStackOverflowRepository stackOverflowLinkRepository;
    @Autowired private JooqLinkService linkService;

    @Test
    @Transactional
    @Rollback
    void upsertNewLink() {
        long chatId = 1L;
        chatRepository.add(new Chat(chatId));
        LinkInfoDto link = linkService.add(chatId, TEST_URL);

        LinkStackoverflow stackOverflowLink = new LinkStackoverflow();
        stackOverflowLink.setLinkId(link.getLinkId());
        stackOverflowLink.setIsAnswered(true);
        stackOverflowLink.setAnswersCount(1);
        stackOverflowLink.setCommentsCount(1);

        stackOverflowLinkRepository.upsertLink(stackOverflowLink);
        Optional<LinkStackoverflow> stackOverflowLinkFromRepo = stackOverflowLinkRepository.findById(link.getLinkId());

        assertThat(stackOverflowLinkFromRepo.isPresent()).isTrue();
        assertThat(stackOverflowLinkFromRepo.get()).isEqualTo(stackOverflowLink);
    }

    @Test
    @Transactional
    @Rollback
    void upsertExistingLink() {
        long chatId = 1L;
        chatRepository.add(new Chat(chatId));
        LinkInfoDto link = linkService.add(chatId, TEST_URL);

        LinkStackoverflow oldStackOverflowLink = new LinkStackoverflow();
        oldStackOverflowLink.setLinkId(link.getLinkId());
        oldStackOverflowLink.setIsAnswered(false);
        oldStackOverflowLink.setAnswersCount(0);
        oldStackOverflowLink.setCommentsCount(0);
        stackOverflowLinkRepository.upsertLink(oldStackOverflowLink);

        LinkStackoverflow newStackOverflowLink = new LinkStackoverflow();
        newStackOverflowLink.setLinkId(link.getLinkId());
        newStackOverflowLink.setIsAnswered(true);
        newStackOverflowLink.setAnswersCount(1);
        newStackOverflowLink.setCommentsCount(1);
        stackOverflowLinkRepository.upsertLink(newStackOverflowLink);

        Optional<LinkStackoverflow> stackOverflowLinkFromRepo = stackOverflowLinkRepository.findById(link.getLinkId());

        assertThat(stackOverflowLinkFromRepo.isPresent()).isTrue();
        assertThat(stackOverflowLinkFromRepo.get()).isEqualTo(newStackOverflowLink);
    }

    @Test
    void findNonExistingLink() {
        Optional<LinkStackoverflow> stackOverflowLinkFromRepo = stackOverflowLinkRepository.findById(1L);
        assertThat(stackOverflowLinkFromRepo.isEmpty()).isTrue();
    }
}
