package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.client.BotHttpClient;
import edu.java.configuration.ClientConfiguration;
import edu.java.configuration.RetryConfiguration;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
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
@ContextConfiguration(initializers = BotClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BotClientTest {
    private static final String URL_UPDATES = "/updates";
    private static final int RETRY_COUNT = 5;
    private final BotHttpClient client;

    @Autowired
    public BotClientTest(Retry retry) {
        this.client = new BotHttpClient("http://localhost:9876", retry);
    }

    public void setupStubSuccess() {
        stubFor(post(urlEqualTo(URL_UPDATES))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("")));
    }

    public void setupStubServerError() {
        stubFor(get(urlEqualTo(URL_UPDATES))
            .willReturn(aResponse()
                .withStatus(404)
            ));
    }

    public void setupStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(post(urlEqualTo(URL_UPDATES))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(429))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(post(urlEqualTo(URL_UPDATES))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }

    @Test
    public void testSuccess() {
        setupStubSuccess();

        assertDoesNotThrow(() -> client.update(
            1,
            URI.create("https://github.com/ShaDi777/java-course-2024-backend"),
            "None",
            new Long[] {1L}
        ));
    }

    @Test
    public void testNoResponse() {
        setupStubServerError();
        assertThrows(WebClientResponseException.class, () -> client.update(
            1,
            URI.create("https://github.com/ShaDi777/java-course-2024-backend"),
            "None",
            new Long[] {1L}
        ));
    }

    @Test
    public void testRetry() {
        setupStubRetry();

        assertDoesNotThrow(() -> client.update(
            1,
            URI.create("https://github.com/ShaDi777/java-course-2024-backend"),
            "None",
            new Long[] {1L}
        ));
        verify(RETRY_COUNT, postRequestedFor(urlEqualTo(URL_UPDATES)));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=linear").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=1000").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502,503,504,505").applyTo(applicationContext);
        }
    }
}
