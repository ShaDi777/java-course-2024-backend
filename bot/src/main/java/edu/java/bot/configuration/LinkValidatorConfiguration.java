package edu.java.bot.configuration;

import edu.java.bot.services.validator.GitHubLinkValidator;
import edu.java.bot.services.validator.StackOverflowLinkValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

@Configuration
public class LinkValidatorConfiguration {
    @Bean(name = "stackOverflowLinkValidator")
    StackOverflowLinkValidator stackOverflowLinkValidator() {
        return new StackOverflowLinkValidator();
    }

    @Bean
    @Primary
    @DependsOn("stackOverflowLinkValidator")
    GitHubLinkValidator gitHubLinkValidator(StackOverflowLinkValidator next) {
        var validator = new GitHubLinkValidator();
        validator.setNextValidator(next);
        return validator;
    }
}
