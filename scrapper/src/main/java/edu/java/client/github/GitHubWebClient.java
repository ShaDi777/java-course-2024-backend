package edu.java.client.github;

import edu.java.client.dto.GitHubResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;

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
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
