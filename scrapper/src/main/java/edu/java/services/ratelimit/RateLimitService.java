package edu.java.services.ratelimit;

import io.github.bucket4j.Bucket;

public interface RateLimitService {
    Bucket resolveBucket(String ip);
}
