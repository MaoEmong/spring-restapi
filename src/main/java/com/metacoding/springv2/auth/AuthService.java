package com.metacoding.springv2.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception400;
import com.metacoding.springv2._core.handler.ex.Exception401;
import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;
import com.metacoding.springv2.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String login(AuthRequest.LoginDTO reqDTO) {

        User findUser = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception401("Username not found"));

        boolean isSamePassword = bCryptPasswordEncoder.matches(reqDTO.getPassword(), findUser.getPassword());
        if (!isSamePassword) {
            throw new Exception401("Invalid password");
        }

        return JwtUtil.create(findUser);
    }

    @Transactional
    public AuthResponse.DTO join(AuthRequest.JoinDTO reqDTO) {

        // rule1 반영: 회원가입 시 유저네임 중복이면 400 에러 처리
        userRepository.findByUsername(reqDTO.getUsername())
                .ifPresent(user -> {
                    throw new Exception400("Username already exists");
                });

        String encPassword = bCryptPasswordEncoder.encode(reqDTO.getPassword());

        User user = User.builder()
                .username(reqDTO.getUsername())
                .password(encPassword)
                .email(reqDTO.getEmail())
                .roles("USER")
                .build();
        userRepository.save(user);

        return new AuthResponse.DTO(user);
    }

    // rule1 반영: AuthController의 유저네임 중복체크 API에서 사용
    public AuthResponse.UsernameCheckDTO checkUsername(String username) {
        boolean available = userRepository.findByUsername(username).isEmpty();
        return new AuthResponse.UsernameCheckDTO(username, available);
    }
}
