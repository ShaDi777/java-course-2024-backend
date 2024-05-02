package edu.java.bot.utils;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    private final int maxAttempts;
    private final Duration minDelay;
    private Predicate<Throwable> filter;
    private BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator;

    public LinearRetry(int maxAttempts, Duration minDelay) {
        this.maxAttempts = maxAttempts;
        this.minDelay = minDelay;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return flux.flatMap(this::generateRetry);
    }

    public LinearRetry filter(Predicate<Throwable> filter) {
        this.filter = filter;
        return this;
    }

    public LinearRetry onRetryExhaustedThrow(
        BiFunction<LinearRetry, RetrySignal, Throwable> retryExhaustedGenerator
    ) {
        this.retryExhaustedGenerator = retryExhaustedGenerator;
        return this;
    }

    private Mono<Long> generateRetry(RetrySignal rs) {
        RetrySignal copy = rs.copy();

        if (!this.filter.test(rs.failure())) {
            return Mono.error(rs.failure());
        }

        if (rs.totalRetries() < maxAttempts) {
            Duration delay = minDelay.multipliedBy(rs.totalRetries());
            return Mono.delay(delay).thenReturn(rs.totalRetries());
        } else {
            return Mono.error(this.retryExhaustedGenerator.apply(this, copy));
        }
    }
}
