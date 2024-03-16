package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.dao.JdbcLinkRepository;
import edu.java.domain.jdbc.model.Link;
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
public class JdbcLinkRepositoryTest extends IntegrationTest {
    private static final String TEST_LINK = "https://github.com/ShaDi777/java-course-2024-backend";

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void addNewLink() {
        Link link = Link.builder().linkId(1L).url(TEST_LINK).build();

        Link savedLink = linkRepository.save(link);

        var links = linkRepository.findAll();
        assertThat(links).contains(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void addExistingLink() {
        Link link = Link.builder().linkId(1L).url(TEST_LINK).build();

        Link savedLink = linkRepository.save(link);
        linkRepository.save(link);

        var links = linkRepository.findAll();
        assertThat(links).containsOnlyOnce(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void deleteExistingLink() {
        Link link = Link.builder().linkId(1L).url(TEST_LINK).build();

        Link savedLink = linkRepository.save(link);

        linkRepository.deleteById(savedLink.getLinkId());

        var chats = linkRepository.findAll();
        assertThat(chats).doesNotContain(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void deleteNonExistingLink() {
        long linkId = 1L;

        linkRepository.deleteById(linkId);

        var chats = linkRepository.findAll();
        assertThat(chats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findExistingLink() {
        Link link = Link.builder().url(TEST_LINK).build();
        Link savedLink = linkRepository.save(link);

        Optional<Link> foundLinkById = linkRepository.findById(savedLink.getLinkId());
        Optional<Link> foundLinkByUrl = linkRepository.findByUrl(TEST_LINK);

        assertThat(foundLinkById).isPresent();
        assertThat(foundLinkByUrl).isPresent();
        assertThat(foundLinkById.get()).isEqualTo(foundLinkByUrl.get());
    }

    @Test
    @Transactional
    @Rollback
    void findNotExistingLink() {
        long linkId = 1L;

        Optional<Link> foundLinkById = linkRepository.findById(linkId);
        Optional<Link> foundLinkByUrl = linkRepository.findByUrl(TEST_LINK);
        assertThat(foundLinkById).isEmpty();
        assertThat(foundLinkByUrl).isEmpty();
    }
}
