package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SlidingWindowCounterRateLimiter extends RateLimiter {
    private final ConcurrentMap<Integer, Bucket> buckets = new ConcurrentHashMap<>();

    public SlidingWindowCounterRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.SLIDING_WINDOW_COUNTER);
    }

    @Override
    public boolean allowRequest(int userId) {
        boolean[] allowed = {false};
        buckets.compute(
                userId, (_, bucket) -> {
                    long now = System.currentTimeMillis();
                    if (bucket == null) {
                        allowed[0] = true;
                        return new Bucket(0, 1, now);
                    }

                    long windowStart = bucket.windowStartTime();
                    long windowInMillis = getConfig().windowInSecs() * 1000L;
                    int prevRequests = bucket.prevRequests();
                    int currRequests = bucket.currRequests();

                    if ((bucket.windowStartTime() + windowInMillis) <= now) {
                        long windowPassed = (now - windowStart) / windowInMillis;
                        windowStart += windowPassed * windowInMillis;
                        prevRequests = windowPassed == 1 ? currRequests : 0;
                    }

                    int estimatedRequests = currRequests +
                            (int) (((double) (windowInMillis - (now - windowStart)) / windowInMillis) * prevRequests);

                    if (estimatedRequests < getConfig().maxRequests()) {
                        currRequests++;
                        allowed[0] = true;
                    }

                    return new Bucket(prevRequests, currRequests, windowStart);

                }
        );


        return allowed[0];
    }

    private record Bucket(int prevRequests, int currRequests, long windowStartTime) {
    }
}
