package edu.java.bot.services.ratelimit;

import edu.java.bot.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixedRateLimitService implements RateLimitService {
    private final ApplicationConfig appConfig;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        ApplicationConfig.RateLimiter rateLimiter = appConfig.rateLimiter();

        Bandwidth limit = rateLimiter.enable()
                          ? Bandwidth.builder()
                                   .capacity(rateLimiter.limit())
                                   .refillIntervally(rateLimiter.refillPerMinute(), Duration.ofMinutes(1))
                                   .initialTokens(rateLimiter.limit())
                                   .build()
                          : Bandwidth.builder()
                                     .capacity(Long.MAX_VALUE)
                                     .refillGreedy(Long.MAX_VALUE, Duration.ofSeconds(1))
                                     .initialTokens(Long.MAX_VALUE)
                                     .build();

        return Bucket.builder()
                     .addLimit(limit)
                     .build();
    }
}
