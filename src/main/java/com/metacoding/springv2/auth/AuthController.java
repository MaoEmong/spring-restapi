package com.metacoding.springv2.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}
