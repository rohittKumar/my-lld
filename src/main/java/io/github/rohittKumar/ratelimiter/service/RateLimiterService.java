package io.github.rohittKumar.ratelimiter.service;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.enums.UserTier;
import io.github.rohittKumar.ratelimiter.factory.RateLimiterFactory;
import io.github.rohittKumar.ratelimiter.limiters.RateLimiter;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;
import io.github.rohittKumar.ratelimiter.model.User;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RateLimiterService {
    private final Map<UserTier, RateLimiter> rateLimiters = new EnumMap<>(UserTier.class);

    public RateLimiterService() {
        rateLimiters.put(
                UserTier.FREE,
                RateLimiterFactory.getRateLimiter(
                        RateLimiterType.TOKEN_BUCKET,
                        new RateLimiterConfig(10, 60)
                )
        );

        rateLimiters.put(
                UserTier.PREMIUM,
                RateLimiterFactory.getRateLimiter(
                        RateLimiterType.TOKEN_BUCKET,
                        new RateLimiterConfig(100, 60)
                )
        );
    }

    public boolean allowRequest(User user) {
        RateLimiter rateLimiter = rateLimiters.get(user.tier());
        return rateLimiter.allowRequest(user.id());
    }
}
