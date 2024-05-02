package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperLinksHttpClient;
import edu.java.bot.client.ScrapperTgChatsHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperLinksHttpClient scrapperLinksHttpClient(Retry retry) {
        return new ScrapperLinksHttpClient(retry);
    }

    @Bean
    public ScrapperTgChatsHttpClient scrapperTgChatsHttpClient(Retry retry) {
        return new ScrapperTgChatsHttpClient(retry);
    }
}
