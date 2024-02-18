package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.GitHubResponse;
import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
public class GitHubClientTest {
    private static final String OWNER = "ShaDi777";
    private static final String REPO = "java-course-2024-backend";
    private static final String URL_SCENARIO = "/repos/" + OWNER + "/" + REPO;
    private static final OffsetDateTime LAST_MODIFIED = OffsetDateTime.of(2024, 2, 18, 23, 59, 59, 0, ZoneOffset.UTC);

    private final GitHubClient client = new GitHubWebClient("http://localhost:9876");

    public void setupStubSuccess() {
        stubFor(get(urlEqualTo(URL_SCENARIO))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                            "title": "%s",
                            "updated_at": %d
                        }
                        """.formatted(REPO, LAST_MODIFIED.toEpochSecond()))
            ));
    }

    public void setupStubServerError() {
        stubFor(get(urlEqualTo(URL_SCENARIO))
            .willReturn(aResponse()
                .withStatus(505)
            ));
    }

    @Test
    public void testSuccess() {
        setupStubSuccess();
        GitHubResponse response = client.fetchRepository(OWNER, REPO);
        assertThat(response.title()).isEqualTo(REPO);
        assertThat(response.lastModified()).isEqualTo(LAST_MODIFIED);
    }

    @Test
    public void testNoResponse() {
        setupStubServerError();
        assertThrows(WebClientResponseException.class, () -> client.fetchRepository(OWNER, REPO));
    }
}
