package org.example.pulsebackend.features.email;

import org.example.pulsebackend.features.email.dto.InviteEmailDto;
import org.example.pulsebackend.features.email.dto.WelcomeEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/invite")
    public void sendInviteEmail(@RequestBody InviteEmailDto request) {
            emailService.sendInviteEmail(request);
    }

    @PostMapping("/welcome")
    public void sendWelcomeEmail(@RequestBody WelcomeEmailDto request) {
        emailService.sendWelcomeEmail(request);
    }
}
