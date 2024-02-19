package edu.java.client.stackoverflow;

import edu.java.client.dto.StackOverflowResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;

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
            .uri("/questions/{questionId}", questionId)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }
}
