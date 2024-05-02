package edu.java.bot.configuration;

import edu.java.bot.utils.BackOffType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    RateLimiter rateLimiter,
    RetrySpecification retrySpecification,
    TopicInfo topic,
    TopicInfo deadTopic
) {
    public record RateLimiter(
        boolean enable,
        @NotNull Integer limit,
        @NotNull Integer refillPerMinute
    ) { }

    public record RetrySpecification(
        @NotNull BackOffType backOffType,
        @NotNull Integer maxAttempts,
        @NotNull Duration delay,
        @NotNull Double jitter,
        Set<Integer> codes
    ) {
    }

    public record TopicInfo(
        @NotNull String name,
        @NotNull Integer partitions,
        @NotNull Integer replicas
    ) {
    }
}
