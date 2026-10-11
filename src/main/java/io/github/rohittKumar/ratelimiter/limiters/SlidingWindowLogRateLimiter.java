package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

public class SlidingWindowLogRateLimiter extends RateLimiter {
    public SlidingWindowLogRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.SLIDING_WINDOW_LOG);
    }

    @Override
    public boolean allowRequest(int userId) {
        return false;
    }
}
