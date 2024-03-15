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
    @Bean(name = "stackOverflowApiHandler")
    public StackOverflowApiHandler stackOverflowApiHandler(
        StackOverflowClient stackOverflowClient,
        StackOverflowLinkService stackOverflowLinkService
    ) {
        return new StackOverflowApiHandler(stackOverflowClient, stackOverflowLinkService);
    }

    @Bean
    @Primary
    @DependsOn("stackOverflowApiHandler")
    public GitHubApiHandler gitHubApiHandler(
        GitHubClient gitHubClient,
        StackOverflowApiHandler stackOverflowApiHandler
    ) {
        var gitHubApiHandler = new GitHubApiHandler(gitHubClient);
        gitHubApiHandler.setNext(stackOverflowApiHandler);
        return gitHubApiHandler;
    }
}
