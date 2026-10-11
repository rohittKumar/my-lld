package io.github.rohittKumar.ratelimiter.model;

import io.github.rohittKumar.ratelimiter.enums.UserTier;

public record User(int id, UserTier tier) {
}
