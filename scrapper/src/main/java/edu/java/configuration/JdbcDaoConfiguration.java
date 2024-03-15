package edu.java.configuration;

import edu.java.dao.jdbc.JdbcLinkRepository;
import edu.java.dao.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.dao.jdbc.JdbcTgChatRepository;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcStackOverflowLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcDaoConfiguration {
    @Bean
    public JdbcTgChatService jdbcChatService(JdbcTgChatRepository chatRepository) {
        return new JdbcTgChatService(chatRepository);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(JdbcLinkRepository linkRepository) {
        return new JdbcLinkService(linkRepository);
    }

    @Bean
    public JdbcStackOverflowLinkService stackOverflowLinkService(
        JdbcStackOverflowLinkRepository stackOverflowLinkRepository
    ) {
        return new JdbcStackOverflowLinkService(stackOverflowLinkRepository);
    }
}
