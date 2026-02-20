package com.metacoding.springv2._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.metacoding.springv2._core.filter.JwtAuthorizationFilter;
import com.metacoding.springv2._core.util.RespFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    // 애플리케이션으로 들어오는 모든 HTTP 요청은 이 SecurityFilterChain 규칙을 따라 처리된다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1) 응답 헤더 정책: 같은 출처(same-origin)에서만 iframe 렌더링을 허용한다.
        // 기본 DENY 정책에서는 iframe 자체가 차단되므로, H2 콘솔처럼 iframe을 쓰는 화면이 있다면 필요하다.
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // 2) CORS: Security 레벨 CORS 처리 비활성화.
        // CORS를 다른 계층(WebMvcConfigurer, 별도 CorsFilter)에서 통합 관리할 때 충돌을 피하기 위한 설정이다.
        http.cors(c -> c.disable());

        // 3) 인증/인가 예외 처리 진입점 구성.
        // - authenticationEntryPoint: 인증 실패(미인증) 시 실행 -> 401
        // - accessDeniedHandler: 인증은 되었지만 권한 부족 시 실행 -> 403
        // 기본 HTML 에러 페이지 대신 API 친화적인 JSON 응답을 내려주기 위해 커스텀 응답을 사용한다.
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                        (request, response, authException) -> RespFilter.fail(response, 401, "로그인 후 이용해주세요"))
                .accessDeniedHandler(
                        (request, response, accessDeniedException) -> RespFilter.fail(response, 403, "권한이 없습니다")));

        // 4) 세션 정책: STATELESS
        // 서버가 인증 상태를 HttpSession에 저장하지 않는다.
        // 즉, 매 요청마다 토큰(JWT)로 사용자를 다시 검증하는 구조가 된다.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 5) 인가(Authorization) 규칙 정의.
        // 요청 URL이 아래 규칙에 매칭되면 해당 조건으로 접근 여부를 결정한다.
        // 선언 순서가 중요하며, 먼저 매칭된 규칙이 우선 적용된다.
        http.authorizeHttpRequests(authorize -> authorize
                // /api/** : 인증된 사용자만 접근 가능
                .requestMatchers("/api/**").authenticated()
                // /admin/** : ROLE_ADMIN 권한이 있어야 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 그 외 나머지 요청: 공개 접근 허용
                .anyRequest().permitAll());

        // 6) 기본 폼 로그인 필터 비활성화.
        // UsernamePasswordAuthenticationFilter 기반의 기본 로그인 엔드포인트를 사용하지 않겠다는 의미다.
        http.formLogin(f -> f.disable());

        // 7) HTTP Basic 인증 비활성화.
        // Authorization: Basic 헤더 기반 인증을 끄고, 토큰 기반 인증 방식으로 단일화한다.
        http.httpBasic(b -> b.disable());

        // 8) CSRF 보호 비활성화.
        // 세션/쿠키 기반 폼 로그인 앱에서 주로 필요한 보호이므로,
        // 현재처럼 무상태(STATELESS) + JWT API 구조에서는 일반적으로 비활성화한다.
        http.csrf(c -> c.disable());

        // 9) JWT 필터 삽입 위치 지정.
        // UsernamePasswordAuthenticationFilter보다 먼저 실행되도록 배치해서,
        // 컨트롤러 진입 전에 JWT 검증 및 SecurityContext 인증 세팅이 완료되게 한다.
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 10) 위 설정을 실제 필터 체인 객체로 빌드하여 스프링 컨테이너에 등록.
        return http.build();
    }
}