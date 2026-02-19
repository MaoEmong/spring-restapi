package com.metacoding.springv2.auth;

import com.metacoding.springv2.auth.AuthRequest.LoginDTO;

public class AuthService {

    public void 로그인(LoginDTO reqDTO) {
        // 1. UserRepository 에서 username 확인 (검증)

        // 2. password를 hash에서 비교검증

        // 3. Authentication에서 객체 만들기

        // 4. SecurityContextHolder에 저장
    }

}