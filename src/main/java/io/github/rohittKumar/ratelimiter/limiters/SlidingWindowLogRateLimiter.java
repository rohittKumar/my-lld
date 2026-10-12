package io.github.rohittKumar.ratelimiter.limiters;

import io.github.rohittKumar.ratelimiter.enums.RateLimiterType;
import io.github.rohittKumar.ratelimiter.model.RateLimiterConfig;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class SlidingWindowLogRateLimiter extends RateLimiter {
    private final ConcurrentMap<Integer, Queue<Long>> idToQueueMap = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(RateLimiterConfig config) {
        super(config, RateLimiterType.SLIDING_WINDOW_LOG);
    }

    @Override
    public boolean allowRequest(int userId) {
        boolean[] allowed = {false};
        idToQueueMap.compute(
                userId, (id, queue) -> {
                    long now = System.currentTimeMillis();

                    if (queue == null) {
                        queue = new ConcurrentLinkedQueue<>();
                    }

                    long windowInMillis = getConfig().windowInSecs() * 1000L;
                    while (!queue.isEmpty() && (now - queue.peek()) > windowInMillis) {
                        queue.poll();
                    }

                    if (queue.size() < getConfig().maxRequests()) {
                        queue.offer(now);
                        allowed[0] = true;
                    }

                    return queue;
                }
        );

        return allowed[0];
    }
}
