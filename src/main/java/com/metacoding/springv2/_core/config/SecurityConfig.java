package com.metacoding.springv2._core.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.metacoding.springv2._core.filter.JwtAuthorizationFilter;
import com.metacoding.springv2._core.util.RespFilter;

@Configuration
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // rule1 반영: CORS 처리 책임을 SecurityConfig로 통합
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (origins.isEmpty() || origins.contains("*")) {
            configuration.setAllowedOriginPatterns(List.of("*"));
        } else {
            configuration.setAllowedOrigins(origins);
        }

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // rule1 반영: Spring Security CORS 활성화
        http.cors(Customizer.withDefaults());

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                        (request, response, authException) -> RespFilter.fail(response, 401, "Login required"))
                .accessDeniedHandler(
                        (request, response, accessDeniedException) -> RespFilter.fail(response, 403, "Forbidden")));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());

        http.formLogin(f -> f.disable());
        http.httpBasic(b -> b.disable());
        http.csrf(c -> c.disable());

        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
