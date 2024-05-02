package edu.java.scrapper.jpa;

import edu.java.domain.jpa.dao.JpaTgChatRepository;
import edu.java.domain.jpa.model.Chat;
import edu.java.domain.jpa.model.StackOverflowLink;
import edu.java.dto.link.LinkInfoDto;
import edu.java.dto.link.StackOverflowLinkDto;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaStackOverflowLinkService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.database-access-type=jpa")
@Testcontainers
public class JpaStackOverflowLinkServiceTest extends IntegrationTest {
    public static final String TEST_URL =
        "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array";

    @Autowired private JpaTgChatRepository chatRepository;
    @Autowired private JpaStackOverflowLinkService stackOverflowLinkService;
    @Autowired private JpaLinkService linkService;
    @Autowired private StackOverflowLinkMapper stackOverflowLinkMapper;

    @Test
    @Transactional
    @Rollback
    void upsertNewLink() {
        long chatId = 1L;
        chatRepository.save(new Chat(chatId, null));
        LinkInfoDto link = linkService.add(chatId, TEST_URL);

        StackOverflowLink stackOverflowLink = new StackOverflowLink();
        stackOverflowLink.setLinkId(link.getLinkId());
        stackOverflowLink.setIsAnswered(true);
        stackOverflowLink.setAnswersCount(1);
        stackOverflowLink.setCommentsCount(1);

        stackOverflowLinkService.upsertLink(stackOverflowLinkMapper.jpaModelToDto(stackOverflowLink));
        Optional<StackOverflowLink> stackOverflowLinkFromRepo =
            stackOverflowLinkService.findById(link.getLinkId()).map(stackOverflowLinkMapper::dtoToJpaModel);

        assertThat(stackOverflowLinkFromRepo.isPresent()).isTrue();
        assertThat(stackOverflowLinkFromRepo.get()).isEqualTo(stackOverflowLink);
    }

    @Test
    @Transactional
    @Rollback
    void upsertExistingLink() {
        long chatId = 1L;
        chatRepository.save(new edu.java.domain.jpa.model.Chat(chatId, null));
        LinkInfoDto link = linkService.add(chatId, TEST_URL);

        StackOverflowLink oldStackOverflowLink = new StackOverflowLink();
        oldStackOverflowLink.setLinkId(link.getLinkId());
        oldStackOverflowLink.setIsAnswered(false);
        oldStackOverflowLink.setAnswersCount(0);
        oldStackOverflowLink.setCommentsCount(0);
        stackOverflowLinkService.upsertLink(stackOverflowLinkMapper.jpaModelToDto(oldStackOverflowLink));

        StackOverflowLink newStackOverflowLink = new StackOverflowLink();
        newStackOverflowLink.setLinkId(link.getLinkId());
        newStackOverflowLink.setIsAnswered(true);
        newStackOverflowLink.setAnswersCount(1);
        newStackOverflowLink.setCommentsCount(1);
        stackOverflowLinkService.upsertLink(stackOverflowLinkMapper.jpaModelToDto(newStackOverflowLink));

        Optional<StackOverflowLink> stackOverflowLinkFromRepo =
            stackOverflowLinkService.findById(link.getLinkId()).map(stackOverflowLinkMapper::dtoToJpaModel);

        assertThat(stackOverflowLinkFromRepo.isPresent()).isTrue();
        assertThat(stackOverflowLinkFromRepo.get()).isEqualTo(newStackOverflowLink);
    }

    @Test
    void findNonExistingLink() {
        Optional<StackOverflowLinkDto> stackOverflowLinkFromRepo = stackOverflowLinkService.findById(1L);
        assertThat(stackOverflowLinkFromRepo.isEmpty()).isTrue();
    }
}
