package com.metacoding.springv2.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    // rule1 반영: REST 규칙에 맞춰 /api/users/{id} 단건 조회 제공
    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        var respDTO = userService.findById(id);
        return Resp.ok(respDTO);
    }
}
