package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketRateLimiter extends RateLimiter {
    private final ConcurrentHashMap<Integer, Bucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.TOKEN_BUCKET);
    }

    @Override
    public boolean allowRequest(int userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);

        buckets.compute(
                userId, (_, bucket) -> {
                    long now = System.currentTimeMillis();
                    if (bucket == null) {
                        allowed.set(true);
                        return new Bucket(getConfig().maxRequests() - 1, now);
                    }

                    int tokens = refillTokens(bucket, now);
                    long lastRefillTime = bucket.lastRefillTime();
                    if (tokens > bucket.tokens()) {
                        lastRefillTime = now;
                    }

                    if (tokens > 0) {
                        allowed.set(true);
                        tokens--;
                    }
                    return new Bucket(tokens, lastRefillTime);
                }
        );

        return allowed.get();
    }

    private int refillTokens(Bucket bucket, long now) {
        long elapsedSecs = (now - bucket.lastRefillTime()) / 1000;
        double refillRate = (double) getConfig().windowInSecs() / getConfig().maxRequests();

        return Math.min(
                getConfig().maxRequests(),
                bucket.tokens() + (int) (elapsedSecs / refillRate)
        );
    }

    private record Bucket(int tokens, long lastRefillTime) {
    }
}
