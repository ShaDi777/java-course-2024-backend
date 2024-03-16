package edu.java.scrapper.jooq;

import edu.java.domain.jooq.dao.JooqLinkRepository;
import edu.java.domain.jooq.generated.tables.pojos.Link;
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
public class JooqLinkRepositoryTest extends IntegrationTest {
    private static final String TEST_LINK = "https://github.com/ShaDi777/java-course-2024-backend";

    @Autowired
    private JooqLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void addNewLink() {
        Link link = new Link();
        link.setLinkId(1L);
        link.setUrl(TEST_LINK);

        Link savedLink = linkRepository.add(link);

        var links = linkRepository.findAll();
        assertThat(links).contains(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void addExistingLink() {
        Link link = new Link();
        link.setLinkId(1L);
        link.setUrl(TEST_LINK);

        Link savedLink = linkRepository.add(link);
        linkRepository.add(link);

        var links = linkRepository.findAll();
        assertThat(links).containsOnlyOnce(savedLink);
    }

    @Test
    @Transactional
    @Rollback
    void deleteExistingLink() {
        Link link = new Link();
        link.setLinkId(1L);
        link.setUrl(TEST_LINK);

        Link savedLink = linkRepository.add(link);
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
        Link link = new Link();
        link.setLinkId(1L);
        link.setUrl(TEST_LINK);
        Link savedLink = linkRepository.add(link);

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
