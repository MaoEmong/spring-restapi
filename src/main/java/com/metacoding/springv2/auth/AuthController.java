package com.metacoding.springv2.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody AuthRequest.JoinDTO reqDTO) {
        var respDTO = authService.join(reqDTO);
        return Resp.ok(respDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest.LoginDTO reqDTO) {
        String accessToken = authService.login(reqDTO);
        return Resp.ok(accessToken);
    }

    /**
     * 로그아웃 엔드포인트입니다.
     * JWT 방식의 경우 서버에서 상태를 유지하지 않으므로, 클라이언트가 토큰을 폐기하도록 유도합니다.
     */
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout() {
        return Resp.ok(null);
    }

    // rule1 반영: 회원가입 전 유저네임 중복 여부를 확인하는 엔드포인트
    @GetMapping("/username-same-check")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        var respDTO = authService.checkUsername(username);
        return Resp.ok(respDTO);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}
