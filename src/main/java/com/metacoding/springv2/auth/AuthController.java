package com.metacoding.springv2.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest.LoginDTO reqDTO) { // json 데이터 파싱 (requestbody의 역할)
        authService.로그인(reqDTO);
        return "login까지 옴?";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}