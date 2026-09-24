package org.example.pulsebackend.billing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @GetMapping("/plans")
    public String getBillingPlans() {
        return "Billing";
    }

    @PostMapping("/webhook")
    public String webhook() {
        return "Webhook";
    }

    @PostMapping("/checkout")
    public String checkout() {
        return "Checkout";
    }


}
