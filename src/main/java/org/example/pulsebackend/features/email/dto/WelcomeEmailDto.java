package org.example.pulsebackend.features.email.dto;

public record WelcomeEmailDto(
        String recipientEmail, String recipientName, String loginUrl
) {
}
