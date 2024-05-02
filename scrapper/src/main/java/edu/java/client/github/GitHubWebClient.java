package edu.java.client.github;

import edu.java.dto.github.GitHubResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class GitHubWebClient implements GitHubClient {
    private static final String BASE_URL = "https://api.github.com";

    private final WebClient webClient;
    private final Retry retry;

    public GitHubWebClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public GitHubWebClient(String baseUrl, Retry retry) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.retry = retry;
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
                return response.createError();
            })
            .bodyToMono(GitHubResponse.class)
            .retryWhen(retry)
            .block();
    }
}
