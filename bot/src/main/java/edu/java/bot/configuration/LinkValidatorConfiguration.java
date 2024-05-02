package edu.java.bot.configuration;

import edu.java.bot.services.validator.GitHubLinkValidator;
import edu.java.bot.services.validator.StackOverflowLinkValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

@Configuration
public class LinkValidatorConfiguration {
    private static final String STACKOVERFLOW_VALIDATOR = "stackOverflowLinkValidator";
    private static final String GITHUB_VALIDATOR = "gitHubValidator";

    @Bean(STACKOVERFLOW_VALIDATOR)
    StackOverflowLinkValidator stackOverflowLinkValidator() {
        return new StackOverflowLinkValidator();
    }

    @Bean(GITHUB_VALIDATOR)
    @Primary
    @DependsOn(STACKOVERFLOW_VALIDATOR)
    GitHubLinkValidator gitHubLinkValidator(StackOverflowLinkValidator next) {
        var validator = new GitHubLinkValidator();
        validator.setNextValidator(next);
        return validator;
    }
}
