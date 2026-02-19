package com.metacoding.springv2._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.metacoding.springv2._core.filter.JwtAuthorizationFilter;

@Configuration
public class SecurityConfig {

    // 시큐리티 필터 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 인증/권한 주소 커스터마이징
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()); // permitall = 허용

        // UserPasswordAutenticationFilter 비활성화
        // 폼 로그인의 비활성화 (POST : x-www.form-urlencoded : username, password)
        http.formLogin(f -> f.disable());

        // 기본(basic) 인증 활성화 시킴 (request 할때마다 username, password를 요구)
        http.httpBasic(b -> b.disable());

        // input에 csrf 토큰 받는 것을 비활성화
        http.csrf(c -> c.disable());

        // 인증 필터를 변경
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}