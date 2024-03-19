package edu.java.configuration;

import edu.java.domain.jpa.dao.JpaLinkRepository;
import edu.java.domain.jpa.dao.JpaStackOverflowRepository;
import edu.java.domain.jpa.dao.JpaTgChatRepository;
import edu.java.mapping.LinkMapper;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.services.jpa.JpaLinkChatService;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaStackOverflowLinkService;
import edu.java.services.jpa.JpaTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaDaoConfiguration {
    @Bean
    public JpaLinkService jpaLinkService(
        JpaTgChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        JpaStackOverflowRepository stackOverflowRepository,
        LinkMapper linkMapper
    ) {
        return new JpaLinkService(
            chatRepository,
            linkRepository,
            stackOverflowRepository,
            linkMapper
        );
    }

    @Bean
    public JpaTgChatService jpaTgChatService(
        JpaTgChatRepository chatRepository,
        JpaLinkRepository linkRepository
    ) {
        return new JpaTgChatService(
            chatRepository,
            linkRepository
        );
    }

    @Bean
    public JpaLinkChatService jpaLinkChatService(
        JpaTgChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        LinkMapper linkMapper
    ) {
        return new JpaLinkChatService(
            chatRepository,
            linkRepository,
            linkMapper
        );
    }

    @Bean
    public JpaStackOverflowLinkService jpaStackOverflowLinkService(
        JpaStackOverflowRepository stackOverflowRepository,
        StackOverflowLinkMapper stackOverflowLinkMapper
    ) {
        return new JpaStackOverflowLinkService(
            stackOverflowRepository,
            stackOverflowLinkMapper
        );
    }
}
