package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
public class ScrapperChatsClientTest {
    private static final String URL_TG_CHAT = "/tg-chat/{id}";
    private final ScrapperTgChatsHttpClient client = new ScrapperTgChatsHttpClient("http://localhost:9876");

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
                .withPathParam("id", equalTo("-1"))
                .willReturn(aResponse()
                    .withStatus(404)
                )
        );
        stubFor(
            delete(urlPathTemplate(URL_TG_CHAT))
                .withPathParam("id", equalTo("-1"))
                .willReturn(aResponse()
                    .withStatus(404)
                )
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

        assertThrows(WebClientResponseException.class, () -> client.addTgChat(1L));
        assertThrows(WebClientResponseException.class, () -> client.deleteTgChat(1L));
    }
}
