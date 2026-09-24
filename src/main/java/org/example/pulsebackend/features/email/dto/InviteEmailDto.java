package org.example.pulsebackend.features.email.dto;

public record InviteEmailDto(String recipientEmail, String fromName, String orgName, String inviteLink, String token) {
}
