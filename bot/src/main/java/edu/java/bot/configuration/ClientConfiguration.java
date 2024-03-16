package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperLinksHttpClient;
import edu.java.bot.client.ScrapperTgChatsHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperLinksHttpClient scrapperLinksHttpClient() {
        return new ScrapperLinksHttpClient();
    }

    @Bean
    public ScrapperTgChatsHttpClient scrapperTgChatsHttpClient() {
        return new ScrapperTgChatsHttpClient();
    }
}
