package edu.java.client.stackoverflow;

import edu.java.client.dto.StackOverflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
public class StackOverflowWebClient implements StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3/";
    private final WebClient webClient;

    public StackOverflowWebClient() {
        this(BASE_URL);
    }

    public StackOverflowWebClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public StackOverflowResponse fetchQuestion(@NotNull Long questionId) {
        return this.webClient
            .get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, (response) -> Mono.empty())
            .onStatus(HttpStatusCode::is5xxServerError, (response) -> {
                log.error("StackOverflow server error. Status code: " + response.statusCode());
                return Mono.empty();
            })
            .bodyToMono(StackOverflowResponse.class)
            .onErrorMap(WebClientResponseException.class, (throwable) -> {
                log.error("WebclientResponseException: " + throwable.getResponseBodyAsString());
                return null;
            })
            .onErrorMap(Exception.class, (throwable) -> {
                log.error("Unknown exception occurred! " + throwable.getMessage());
                return null;
            })
            .block();
    }
}
