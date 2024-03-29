package edu.java.configuration;

import edu.java.utils.BackOffType;
import edu.java.utils.DatabaseAccessType;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    DatabaseAccessType databaseAccessType,
    RateLimiter rateLimiter,
    RetrySpecification retrySpecification
) {
    public record Scheduler(
        boolean enable,
        @NotNull Duration interval,
        @NotNull Duration forceCheckDelay
    ) { }

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
}
