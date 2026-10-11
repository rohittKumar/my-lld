package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

public class LeakyBucketRateLimiter extends RateLimiter {
    public LeakyBucketRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.LEAKY_BUCKET);
    }

    @Override
    public boolean allowRequest(int userId) {
        return false;
    }
}
