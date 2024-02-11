package edu.java.bot;

import edu.java.bot.linktracker.links.LinkRepository;
import edu.java.bot.linktracker.links.TemporaryLinkRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkValidationTests {
    private final LinkRepository linkRepository = new TemporaryLinkRepository();

    @ParameterizedTest
    @ValueSource(
        strings = {
            "https://github.com",
            "https://github.com/ShaDi777/java-course-2023/"
        }
    )
    public void validLinks(String link) {
        boolean result = linkRepository.isValidLink(link);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "",
            "123",
            "abc",
            "github.com",
        }
    )
    public void invalidLinks(String link) {
        boolean result = linkRepository.isValidLink(link);
        assertThat(result).isFalse();
    }
}
