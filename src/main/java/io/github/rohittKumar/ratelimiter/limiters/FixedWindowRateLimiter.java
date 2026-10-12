package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class FixedWindowRateLimiter extends RateLimiter {
    ConcurrentMap<Integer, Bucket> buckets = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.FIXED_WINDOW);
    }

    @Override
    public boolean allowRequest(int userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        buckets.compute(
                userId, (_, bucket) -> {
                    long now = System.currentTimeMillis();
                    if (bucket == null) {
                        allowed.set(true);
                        return new Bucket(1, now);
                    }

                    long startTime = bucket.windowStartTime();
                    long endTIme = startTime + getConfig().windowInSecs() * 1000L;

                    if (now < endTIme) {
                        if (bucket.requests() < getConfig().maxRequests()) {
                            allowed.set(true);
                            return new Bucket(bucket.requests() + 1, startTime);
                        }
                    } else {
                        allowed.set(true);
                        return new Bucket(1, now);
                    }

                    return bucket;
                }
        );

        return allowed.get();
    }

    private record Bucket(int requests, long windowStartTime) {
    }
}
