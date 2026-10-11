package io.github.rohittKumar.ratelimiter.factory;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.limiters.*;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

public class RateLimiterFactory {
    private RateLimiterFactory() {
    }

    public static RateLimiter getRateLimiter(RateLimiterType type, RateLimiterConfig config) {
        return switch (type) {
            case TOKEN_BUCKET -> new TokenBucketRateLimiter(config);
            case LEAKY_BUCKET -> new LeakyBucketRateLimiter(config);
            case FIXED_WINDOW -> new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW_LOG -> new SlidingWindowLogRateLimiter(config);
            case SLIDING_WINDOW_COUNTER -> new SlidingWindowCounterRateLimiter(config);
            default -> throw new IllegalArgumentException("invalid rate limiter type");
        };
    }
}
