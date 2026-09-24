package org.example.pulsebackend.features.paystack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaystackService {
    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    private final RestTemplate   restTemplate = new RestTemplate();

    public Map<String, Object> initializeTransaction(String email, BigDecimal amount) {
        String url = baseUrl + "/transaction/initialize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + secretKey);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        // Paystack expects amount in kobo (multiply main currency value by 100)
        body.put("amount", amount.multiply(BigDecimal.valueOf(100)));
        body.put("callback_url", "https://pulse-epicordia.web.app/");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();
    }

    public Map<String, Object> verifyTransaction(String reference) {
        String url = baseUrl + "/transaction/verify/" + reference;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);

        // Use HttpEntity since a GET request doesn't need a body payload
        HttpEntity entity = new HttpEntity<>(headers);

        // Use exchange() instead of getForEntity() to pass custom headers
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }

    public Map<String, Object> fetchPlans() {
        String url = baseUrl + "/plan";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);


        // Use HttpEntity since a GET request doesn't need a body payload
        HttpEntity entity = new HttpEntity<>(headers);

        // Use exchange() instead of getForEntity() to pass custom headers
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();

    }

    public Map<String, Object> createPlan(PaymentPlan plan) {
        String url = baseUrl + "/plan";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String, Object> body = new HashMap<>();
        body.put("name", plan.name());
        body.put("description", plan.description());
        body.put("amount", plan.amount());
        body.put("interval", plan.interval().toString());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();


    }
}
