package validation;

import edu.java.bot.services.validator.GitHubLinkValidator;
import edu.java.bot.services.validator.LinkValidator;
import edu.java.bot.services.validator.StackOverflowLinkValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

public class SupportedLinkValidatorTest {
    private final LinkValidator linkValidatorChain;

    SupportedLinkValidatorTest() {
        linkValidatorChain = new GitHubLinkValidator();
        linkValidatorChain.setNextValidator(new StackOverflowLinkValidator());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://github.com/ShaDi777/java-course-2023",
        "http://github.com/ShaDi777/java-course-2024-backend",
        "https://github.com/torvalds/linux",

        "https://stackoverflow.com/questions/52446449/parse-json-using-spring-spel",
        "http://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-processing-an-unsorted-array",
        "https://STACKOVERFLOW.com/Questions/6591213/how-can-i-rename-a-local-git-branch"
    })
    void supportedLink(String url) {
        assertThat(linkValidatorChain.isLinkValid(url)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
        "https://github.com/ShaDi777/java-course-2024-backend/pulls/1",
        "https://stackoverflow.com/beta/discussions/77383225/why-do-people-keep-shouting-php-is-dead-always",
    })
    void unsupportedGitHubLink(String url) {
        assertThat(linkValidatorChain.isLinkValid(url)).isFalse();
    }
}
