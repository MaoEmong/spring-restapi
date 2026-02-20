package com.metacoding.springv2._core.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.metacoding.springv2.user.User;

public class JwtUtil {
    public static final String HEADER = "Authorization"; // HTTP 헤더 이름
    public static final String TOKEN_PREFIX = "Bearer "; // 토큰 접두사
    public static final String SECRET = "메타코딩시크릿키"; // 토큰 서명에 사용될 비밀 키 (강력하게 변경 필요!)
    public static final Long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7; // 토큰 유효기간 7일

    // 생산과 검증의 주체가 같다면 대칭키로 처리가능
    // 생산과 검증의 주체가 다르다면 개인키, 공개키로 처리해야됨
    // 단, RSA를 사용할 시 검증 시간이 오래걸린다는 단점이 있음
    // JWT 토큰 생성
    public static String create(User user) {

        String accessToken = JWT.create()
                .withSubject(user.getUsername()) // 토큰의 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 토큰의 유효 기간
                .withClaim("id", user.getId())
                .withClaim("roles", user.getRoles())
                .sign(Algorithm.HMAC512(SECRET)); // 전자 서명

        return accessToken;
    }

    // JWT 토큰 검증 및 디코딩
    public static User verify(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET))
                .build()
                .verify(jwt); // 토큰 검증

        Integer id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getSubject();
        String roles = decodedJWT.getClaim("roles").asString();

        return User.builder().id(id).username(username).roles(roles).build();
    }
}