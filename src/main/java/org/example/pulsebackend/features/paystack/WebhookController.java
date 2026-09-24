package org.example.pulsebackend.features.paystack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @Value("${paystack.secret-key}")
    private String secretKey;

    @PostMapping("/paystack")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-Paystack-Signature") String paystackSignature,
            @RequestBody String payload) {

        // 1. Validate signature for security
        // String computedSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, secretKey).hmacHex(payload);
        // if (!computedSignature.equals(paystackSignature)) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        // }

        // 2. Parse payload event (e.g., charge.success) and fulfill order
        // JSONObject event = new JSONObject(payload);
        // String eventType = event.getString("event");

        return ResponseEntity.ok("Webhook processed successfully");
    }
}