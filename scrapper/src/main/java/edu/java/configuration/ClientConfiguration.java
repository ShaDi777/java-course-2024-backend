package edu.java.configuration;

import edu.java.client.BotHttpClient;
import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfiguration {
    @Bean
    public BotHttpClient botHttpClient(Retry retry) {
        return new BotHttpClient(retry);
    }

    @Bean
    public GitHubClient gitHubWebClient(Retry retry) {
        return new GitHubWebClient(retry);
    }

    @Bean
    public StackOverflowClient stackOverflowWebClient(Retry retry) {
        return new StackOverflowWebClient(retry);
    }
}
