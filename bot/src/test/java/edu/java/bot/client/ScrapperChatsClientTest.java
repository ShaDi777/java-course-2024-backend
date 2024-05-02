package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.bot.configuration.RetryConfiguration;
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
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
@SpringBootTest(classes = {RetryConfiguration.class})
@ContextConfiguration(initializers = ScrapperChatsClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScrapperChatsClientTest {
    private static final String URL_TG_CHAT = "/tg-chat/{id}";
    private static final int RETRY_COUNT = 5;

    private final ScrapperTgChatsHttpClient client;

    @Autowired
    public ScrapperChatsClientTest(Retry retry) {
        this.client = new ScrapperTgChatsHttpClient("http://localhost:9876", retry);
    }

    public void setupStubSuccess() {
        stubFor(
            post(urlPathTemplate(URL_TG_CHAT))
                .withPathParam("id", equalTo("1"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(""))
        );
        stubFor(
            delete(urlPathTemplate(URL_TG_CHAT))
                .withPathParam("id", equalTo("1"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(""))
        );
    }

    public void setupStubServerError() {
        stubFor(
            post(urlPathTemplate(URL_TG_CHAT))
                .withPathParam("id", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(500)
                )
        );
        stubFor(
            delete(urlPathTemplate(URL_TG_CHAT))
                .withPathParam("id", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(500)
                )
        );
    }

    public void setupPostStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(post(urlPathTemplate(URL_TG_CHAT))
                        .withPathParam("id", equalTo("1"))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(502))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(post(urlPathTemplate(URL_TG_CHAT))
                    .withPathParam("id", equalTo("1"))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }

    public void setupDeleteStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(delete(urlPathTemplate(URL_TG_CHAT))
                        .withPathParam("id", equalTo("1"))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(502))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(delete(urlPathTemplate(URL_TG_CHAT))
                    .withPathParam("id", equalTo("1"))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }

    @Test
    public void testSuccess() {
        setupStubSuccess();

        assertDoesNotThrow(() -> client.addTgChat(1L));
        assertDoesNotThrow(() -> client.deleteTgChat(1L));
    }

    @Test
    public void testNoResponse() {
        setupStubServerError();

        assertThat(client.addTgChat(1L)).isNull();
        assertThat(client.deleteTgChat(1L)).isNull();
    }

    @Test
    public void retryPost() {
        setupPostStubRetry();

        assertDoesNotThrow(() -> client.addTgChat(1L));
        verify(RETRY_COUNT, postRequestedFor(urlPathTemplate(URL_TG_CHAT)).withPathParam("id", equalTo("1")));
    }

    @Test
    public void retryDelete() {
        setupDeleteStubRetry();

        assertDoesNotThrow(() -> client.deleteTgChat(1L));
        verify(RETRY_COUNT, deleteRequestedFor(urlPathTemplate(URL_TG_CHAT)).withPathParam("id", equalTo("1")));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=exponential").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=500").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502").applyTo(applicationContext);
        }
    }
}
