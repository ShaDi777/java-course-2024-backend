package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.dto.ScrapperLinkResponse;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonSchema;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
public class ScrapperLinksClientTest {
    private static final String URL_LINKS = "/links";
    private final ScrapperLinksHttpClient client = new ScrapperLinksHttpClient("http://localhost:9876");

    private static final String url1 = "https://github.com/ShaDi777/java-course-2023";
    private static final String url2 = "https://github.com/ShaDi777/java-course-2024-backend";

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
                    .withStatus(404)
                )
        );
        stubFor(
            post(urlEqualTo(URL_LINKS))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );
        stubFor(
            delete(urlEqualTo(URL_LINKS))
                .willReturn(aResponse()
                    .withStatus(404)
                )
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
    public void failedGetRequest() {
        setupStubServerError();

        assertThrows(WebClientResponseException.class, () -> client.getLinks(1L));
        assertThrows(WebClientResponseException.class, () -> client.addLink(1L, url1));
        assertThrows(WebClientResponseException.class, () -> client.deleteLink(1L, url1));
    }
}
