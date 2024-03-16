package edu.java.scrapper;

import edu.java.utils.LinkUtils;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkUtilsTest {
    private static Stream<Arguments> provideUrlWithDomains() {
        return Stream.of(
            Arguments.of("github.com", "github.com"),
            Arguments.of("github.com/ShaDi777/java-course-2024-backend", "github.com"),
            Arguments.of("https://github.com", "github.com"),
            Arguments.of("http://github.com/ShaDi777/java-course-2024-backend/pulls/1", "github.com"),
            Arguments.of("https://www.baeldung.com/spring-security-basic-authentication", "www.baeldung.com")
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrlWithDomains")
    void getDomain(String url, String domain) {
        assertThat(LinkUtils.getDomainName(url)).isEqualTo(domain);
    }

    private static Stream<Arguments> provideUrlWithExtensionList() {
        return Stream.of(
            Arguments.of(
                "github.com",
                List.of()
            ),
            Arguments.of(
                "github.com/ShaDi777/java-course-2024-backend",
                List.of("ShaDi777", "java-course-2024-backend")
            ),
            Arguments.of(
                "https://github.com",
                List.of()
            ),
            Arguments.of(
                "http://github.com/ShaDi777/java-course-2024-backend/pulls/1",
                List.of("ShaDi777", "java-course-2024-backend", "pulls", "1")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrlWithExtensionList")
    void getExtensions(String url, List<String> extensions) {
        assertThat(LinkUtils.getExtensions(url)).containsExactlyElementsOf(extensions);
    }
}

