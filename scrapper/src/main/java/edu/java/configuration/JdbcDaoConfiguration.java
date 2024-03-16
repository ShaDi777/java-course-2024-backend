package edu.java.configuration;

import edu.java.domain.jdbc.dao.JdbcLinkChatRepository;
import edu.java.domain.jdbc.dao.JdbcLinkRepository;
import edu.java.domain.jdbc.dao.JdbcStackOverflowLinkRepository;
import edu.java.domain.jdbc.dao.JdbcTgChatRepository;
import edu.java.mapping.LinkMapper;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.services.jdbc.JdbcLinkChatService;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcStackOverflowLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcDaoConfiguration {
    @Bean
    public JdbcTgChatService jdbcChatService(
        JdbcLinkRepository linkRepository,
        JdbcTgChatRepository chatRepository,
        JdbcLinkChatRepository linkChatRepository
    ) {
        return new JdbcTgChatService(linkRepository, chatRepository, linkChatRepository);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(
        JdbcTgChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcLinkChatRepository linkChatRepository,
        LinkMapper linkMapper
    ) {
        return new JdbcLinkService(chatRepository, linkRepository, linkChatRepository, linkMapper);
    }

    @Bean
    public JdbcLinkChatService jdbcLinkChatService(
        JdbcLinkChatRepository linkChatRepository,
        LinkMapper linkMapper
    ) {
        return new JdbcLinkChatService(linkChatRepository, linkMapper);
    }

    @Bean
    public JdbcStackOverflowLinkService stackOverflowLinkService(
        JdbcStackOverflowLinkRepository stackOverflowLinkRepository,
        StackOverflowLinkMapper stackOverflowLinkMapper
    ) {
        return new JdbcStackOverflowLinkService(stackOverflowLinkRepository, stackOverflowLinkMapper);
    }
}
