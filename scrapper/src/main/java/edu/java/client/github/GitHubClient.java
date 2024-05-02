package edu.java.client.github;

import edu.java.dto.github.GitHubResponse;
import org.jetbrains.annotations.NotNull;

public interface GitHubClient {
    GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repository);
}
