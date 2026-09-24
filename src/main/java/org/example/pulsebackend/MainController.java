package org.example.pulsebackend;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/health")
    public String health() {
        return "Healthy!";
    }

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }
}
