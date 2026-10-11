package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;


public class FixedWindowRateLimiter extends RateLimiter {
    public FixedWindowRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.FIXED_WINDOW);
    }

    @Override
    public boolean allowRequest(int userId) {
        return false;
    }
}
