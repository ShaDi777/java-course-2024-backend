package edu.java.bot.services.ratelimit;

import io.github.bucket4j.Bucket;

public interface RateLimitService {
    Bucket resolveBucket(String ip);
}
