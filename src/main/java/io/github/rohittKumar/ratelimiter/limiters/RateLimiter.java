package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

public abstract class RateLimiter {
    private final RateLimiterConfig config;
    private final RateLimiterType type;

    protected RateLimiter(RateLimiterConfig config, RateLimiterType type) {
        this.config = config;
        this.type = type;
    }

    public abstract boolean allowRequest(int userId);

    protected RateLimiterConfig getConfig() {
        return config;
    }

    public RateLimiterType getType() {
        return type;
    }
}
