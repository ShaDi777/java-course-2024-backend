package edu.java.configuration;

import edu.java.domain.jooq.dao.JooqLinkChatRepository;
import edu.java.domain.jooq.dao.JooqLinkRepository;
import edu.java.domain.jooq.dao.JooqStackOverflowRepository;
import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.mapping.LinkMapper;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.services.jooq.JooqLinkChatService;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqStackOverflowService;
import edu.java.services.jooq.JooqTgChatService;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqDaoConfiguration {
    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public JooqLinkService jooqLinkService(
        JooqTgChatRepository chatRepository,
        JooqLinkRepository linkRepository,
        JooqLinkChatRepository linkChatRepository,
        LinkMapper linkMapper
    ) {
        return new JooqLinkService(
            chatRepository, linkRepository,
            linkChatRepository,
            linkMapper
        );
    }

    @Bean
    public JooqTgChatService jooqTgChatService(
        JooqLinkRepository linkRepository,
        JooqTgChatRepository chatRepository,
        JooqLinkChatRepository linkChatRepository
    ) {
        return new JooqTgChatService(
            linkRepository,
            chatRepository,
            linkChatRepository
        );
    }

    @Bean
    public JooqLinkChatService jooqLinkChatService(
        JooqLinkRepository linkRepository,
        JooqLinkChatRepository linkChatRepository,
        LinkMapper linkMapper
    ) {
        return new JooqLinkChatService(
            linkRepository,
            linkChatRepository,
            linkMapper
        );
    }

    @Bean
    public JooqStackOverflowService jooqStackOverflowService(
        JooqStackOverflowRepository stackOverflowLinkRepository,
        StackOverflowLinkMapper stackOverflowLinkMapper
    ) {
        return new JooqStackOverflowService(
            stackOverflowLinkRepository,
            stackOverflowLinkMapper
        );
    }
}
