package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.services.StackOverflowLinkService;
import edu.java.services.apihandler.GitHubApiHandler;
import edu.java.services.apihandler.StackOverflowApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApiHandlerConfig {
    private static final String STACKOVERFLOW_HANDLER = "stackOverflowApiHandler";
    private static final String GITHUB_HANDLER = "gitHubApiHandler";

    @Bean(STACKOVERFLOW_HANDLER)
    public StackOverflowApiHandler stackOverflowApiHandler(
        StackOverflowClient stackOverflowClient,
        StackOverflowLinkService stackOverflowLinkService
    ) {
        return new StackOverflowApiHandler(stackOverflowClient, stackOverflowLinkService);
    }

    @Bean(GITHUB_HANDLER)
    @Primary
    @DependsOn(STACKOVERFLOW_HANDLER)
    public GitHubApiHandler gitHubApiHandler(
        GitHubClient gitHubClient,
        StackOverflowApiHandler stackOverflowApiHandler
    ) {
        var gitHubApiHandler = new GitHubApiHandler(gitHubClient);
        gitHubApiHandler.setNext(stackOverflowApiHandler);
        return gitHubApiHandler;
    }
}
