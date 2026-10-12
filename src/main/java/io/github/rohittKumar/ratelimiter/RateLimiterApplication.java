package io.github.rohittKumar.ratelimiter;

import io.github.rohittKumar.ratelimiter.enums.UserTier;
import io.github.rohittKumar.ratelimiter.model.User;
import io.github.rohittKumar.ratelimiter.service.RateLimiterService;

public class RateLimiterApplication {
    public static void main(String[] args) throws InterruptedException {
        User freeUser = new User(1, UserTier.FREE);
        User premiumUser = new User(2, UserTier.PREMIUM);

        RateLimiterService rateLimiterService = new RateLimiterService();

        for (int i = 1; i <= 20; i++) {
            if (i == 3) Thread.sleep(5000);
            boolean allowed = rateLimiterService.allowRequest(freeUser);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "REJECTED"));

            Thread.sleep(500);
        }

        System.out.println("*** Premium User ***");

        for (int i = 1; i <= 200; i++) {
            boolean allowed = rateLimiterService.allowRequest(premiumUser);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "REJECTED"));

            Thread.sleep(100);
        }
    }
}
