package io.github.rohittKumar.ratelimiter.model;

public record RateLimiterConfig(int maxRequests, int windowInSecs) {
}
