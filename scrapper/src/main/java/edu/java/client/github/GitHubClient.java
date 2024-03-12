package edu.java.client.github;

import edu.java.client.dto.GitHubResponse;
import org.jetbrains.annotations.NotNull;

public interface GitHubClient {
    GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repository);
}
