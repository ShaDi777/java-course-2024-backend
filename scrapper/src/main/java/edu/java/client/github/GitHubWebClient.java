package edu.java.client.github;

import edu.java.dto.github.GitHubResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
public class GitHubWebClient implements GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubWebClient() {
        this(BASE_URL);
    }

    public GitHubWebClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repository) {
        return this.webClient
            .get()
            .uri("/repos/{owner}/{repository}", owner, repository)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, (response) -> Mono.empty())
            .onStatus(HttpStatusCode::is5xxServerError, (response) -> {
                log.error("Github server error. Status code: " + response.statusCode());
                return Mono.empty();
            })
            .bodyToMono(GitHubResponse.class)
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
