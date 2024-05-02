package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.client.BotHttpClient;
import edu.java.configuration.ClientConfiguration;
import edu.java.configuration.RetryConfiguration;
import edu.java.dto.github.GitHubResponse;
import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
@SpringBootTest(
    classes = {RetryConfiguration.class},
    properties = {
        "app.scheduler.enable=false",
        "app.scheduler.interval=0",
        "app.scheduler.force-check-delay=0",
    }
)
@ContextConfiguration(initializers = GitHubClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GitHubClientTest {
    private static final String OWNER = "ShaDi777";
    private static final String REPO = "java-course-2024-backend";
    private static final String URL_SCENARIO = "/repos/" + OWNER + "/" + REPO;
    private static final OffsetDateTime LAST_MODIFIED = OffsetDateTime.of(2024, 2, 18, 23, 59, 59, 0, ZoneOffset.UTC);

    private static final int RETRY_COUNT = 3;
    private final GitHubClient client;

    @Autowired
    public GitHubClientTest(Retry retry) {
        this.client = new GitHubWebClient("http://localhost:9876", retry);
    }

    public void setupStubSuccess() {
        stubFor(get(urlEqualTo(URL_SCENARIO))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                            "name": "%s",
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

    public void setupStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(get(urlEqualTo(URL_SCENARIO))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(501))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(get(urlEqualTo(URL_SCENARIO))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
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

    @Test
    public void testRetry() {
        setupStubRetry();

        assertDoesNotThrow(() -> client.fetchRepository(OWNER, REPO));
        verify(RETRY_COUNT, getRequestedFor(urlEqualTo(URL_SCENARIO)));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=fixed").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=1000").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502,503,504").applyTo(applicationContext);
        }
    }
}
