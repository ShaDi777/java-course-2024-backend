package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.bot.client.dto.ScrapperLinkResponse;
import edu.java.bot.configuration.RetryConfiguration;
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
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonSchema;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
@SpringBootTest(classes = {RetryConfiguration.class})
@ContextConfiguration(initializers = ScrapperLinksClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScrapperLinksClientTest {
    private static final String URL_LINKS = "/links";
    private static final String url1 = "https://github.com/ShaDi777/java-course-2023";
    private static final String url2 = "https://github.com/ShaDi777/java-course-2024-backend";
    private static final int RETRY_COUNT = 5;

    private final ScrapperLinksHttpClient client;

    @Autowired
    public ScrapperLinksClientTest(Retry retry) {
        this.client = new ScrapperLinksHttpClient("http://localhost:9876", retry);
    }


    public void setupStubSuccess() {
        stubFor(
            get(urlEqualTo(URL_LINKS))
                .withHeader("Tg-Chat-Id", equalTo("1"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(
                        """
                            {
                              "links": [
                                {
                                  "id": 1,
                                  "url": "%s"
                                },
                                {
                                  "id": 2,
                                  "url": "%s"
                                }
                              ],
                              "size": 2
                            }
                            """.formatted(url1, url2)
                    )
                )
        );
        stubFor(
            post(urlEqualTo(URL_LINKS))
                .withHeader("Tg-Chat-Id", equalTo("1"))
                .withRequestBody(matchingJsonSchema(
                        """
                        {
                            "link": "%s"
                        }
                        """.formatted(url1)
                    )
                )
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(
                        """
                        {
                            "id": 1,
                            "url": "%s"
                        }
                        """.formatted(url1)
                    )
                )
        );
        stubFor(
            delete(urlEqualTo(URL_LINKS))
                .withHeader("Tg-Chat-Id", equalTo("1"))
                .withRequestBody(matchingJsonSchema(
                        """
                        {
                            "link": "%s"
                        }
                        """.formatted(url1)
                    )
                )
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
                    .withBody(
                        """
                        {
                            "id": 1,
                            "url": "%s"
                        }
                        """.formatted(url1)
                    ))
        );
    }

    public void setupStubServerError() {
        stubFor(
            get(urlEqualTo(URL_LINKS))
                .willReturn(aResponse()
                    .withStatus(500)
                )
        );
        stubFor(
            post(urlEqualTo(URL_LINKS))
                .willReturn(aResponse()
                    .withStatus(500)
                )
        );
        stubFor(
            delete(urlEqualTo(URL_LINKS))
                .willReturn(aResponse()
                    .withStatus(500)
                )
        );
    }

    public void setupGetStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(get(urlEqualTo(URL_LINKS))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(502))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(get(urlEqualTo(URL_LINKS))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }

    public void setupPostStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(post(urlEqualTo(URL_LINKS))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(502))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(post(urlEqualTo(URL_LINKS))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }

    public void setupDeleteStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(delete(urlEqualTo(URL_LINKS))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(502))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(delete(urlEqualTo(URL_LINKS))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }


    @Test
    public void successGetLinks() {
        setupStubSuccess();

        var getResponse = client.getLinks(1L);

        assertThat(getResponse.links())
            .contains(new ScrapperLinkResponse(1L, URI.create(url1)))
            .contains(new ScrapperLinkResponse(2L, URI.create(url2)));
        assertThat(getResponse.size()).isEqualTo(2);
    }

    @Test
    public void successAddLink() {
        setupStubSuccess();

        var postResponse = client.addLink(1L, url1);

        assertThat(postResponse.id()).isEqualTo(1L);
        assertThat(postResponse.url()).isEqualTo(URI.create(url1));
    }

    @Test
    public void successDeleteLink() {
        setupStubSuccess();

        var deleteResponse = client.deleteLink(1L, url1);

        assertThat(deleteResponse.id()).isEqualTo(1L);
        assertThat(deleteResponse.url()).isEqualTo(URI.create(url1));
    }


    @Test
    public void failedRequests() {
        setupStubServerError();

        assertThrows(WebClientResponseException.class, () -> client.getLinks(1L));
        assertThrows(WebClientResponseException.class, () -> client.addLink(1L, url1));
        assertThrows(WebClientResponseException.class, () -> client.deleteLink(1L, url1));
    }

    @Test
    public void retryGet() {
        setupGetStubRetry();

        assertDoesNotThrow(() ->  client.getLinks(1L));
        verify(RETRY_COUNT, getRequestedFor(urlEqualTo(URL_LINKS)));
    }

    @Test
    public void retryPost() {
        setupPostStubRetry();

        assertDoesNotThrow(() ->  client.addLink(1L, url1));
        verify(RETRY_COUNT, postRequestedFor(urlEqualTo(URL_LINKS)));
    }

    @Test
    public void retryDelete() {
        setupDeleteStubRetry();

        assertDoesNotThrow(() ->  client.deleteLink(1L, url1));
        verify(RETRY_COUNT, deleteRequestedFor(urlEqualTo(URL_LINKS)));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=fixed").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=500").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502").applyTo(applicationContext);
        }
    }
}
