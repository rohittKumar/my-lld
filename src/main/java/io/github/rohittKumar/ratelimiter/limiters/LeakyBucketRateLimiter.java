package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class LeakyBucketRateLimiter extends RateLimiter {
    private final ConcurrentMap<Integer, Bucket> idToBucketMap = new ConcurrentHashMap<>();

    public LeakyBucketRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.LEAKY_BUCKET);
    }

    @Override
    public boolean allowRequest(int userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);

        idToBucketMap.compute(
                userId, (_, bucket) -> {
                    long now = System.currentTimeMillis();
                    if (bucket == null) {
                        allowed.set(true);
                        return new Bucket(1, now);
                    }

                    int newTokens = leakTokens(bucket, now);
                    long lastLeakTime = bucket.lastLeakTime();
                    if (newTokens < bucket.usedTokens()) {
                        lastLeakTime = now;
                    }

                    if (newTokens < getConfig().maxRequests()) {
                        allowed.set(true);
                        newTokens++;
                    }

                    return new Bucket(newTokens, lastLeakTime);
                }
        );

        return allowed.get();
    }

    private int leakTokens(Bucket bucket, long now) {
        double leakRate = (double) getConfig().windowInSecs() / getConfig().maxRequests();
        long elapsedSecs = (now - bucket.lastLeakTime()) / 1000;
        return Math.max(0, bucket.usedTokens() - (int) (elapsedSecs / leakRate));
    }

    private record Bucket(int usedTokens, long lastLeakTime) {
    }
}
