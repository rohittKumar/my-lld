package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

public class SlidingWindowCounterRateLimiter extends RateLimiter {
    public SlidingWindowCounterRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.SLIDING_WINDOW_COUNTER);
    }

    @Override
    public boolean allowRequest(int userId) {
        return false;
    }
}
