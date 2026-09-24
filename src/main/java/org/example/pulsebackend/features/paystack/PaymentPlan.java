package org.example.pulsebackend.features.paystack;



public record PaymentPlan (
        String name,
        String description,
        int amount,
        Interval interval
) {
}


enum Interval {
    monthly,quarterly, biannually, annually
}
