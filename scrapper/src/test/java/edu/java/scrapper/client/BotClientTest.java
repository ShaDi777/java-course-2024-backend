package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.BotHttpClient;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
public class BotClientTest {
    private static final String URL_UPDATES = "/updates";
    private final BotHttpClient client = new BotHttpClient("http://localhost:9876");

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
}
