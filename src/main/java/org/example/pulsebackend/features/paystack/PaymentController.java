package org.example.pulsebackend.features.paystack;

import java.util.Map; // <-- Use this
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {


    @Autowired
    private PaystackService paystackService;


    // Matches your Test Callback URL redirection path
    @GetMapping("/callback")
    public ResponseEntity<String> handlePaystackCallback(@RequestParam("reference") String reference) {
        // Verify transaction reference using Paystack Verify API endpoint
        // e.g., GET https://api.paystack.co/transaction/verify/{reference}

        return ResponseEntity.ok("Payment received and reference " + reference + " is being verified.");
    }

    @GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> initializeTransaction(
            @RequestParam String email,
            @RequestParam BigDecimal amount) {

        Map<String, Object> response = paystackService.initializeTransaction(email, amount);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyTransaction(@RequestParam String reference) {
        return ResponseEntity.ok(
                paystackService.verifyTransaction(reference)
        );
    }

    @GetMapping("/plan")
    public ResponseEntity<Map<String, Object>> fetchBankProviders() {
        return ResponseEntity.ok(
                paystackService.fetchPlans()
        );
    }

    @PostMapping("/plan")
    public ResponseEntity<Map<String, Object>> createPlan(@RequestBody PaymentPlan plan) {
        return ResponseEntity.ok(
                paystackService.createPlan(plan)
        );
    }

}

class Payload {
    String email;
    BigDecimal amount;
}
