package com.metacoding.springv2.auth;

import com.metacoding.springv2.user.User;

import lombok.Data;

public class AuthResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String username;
        private String roles;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.roles = user.getRoles();
        }
    }

    @Data
    public static class UsernameCheckDTO {
        private String username;
        private boolean available;

        // rule1 반영: 유저네임 중복체크 응답 전용 DTO
        public UsernameCheckDTO(String username, boolean available) {
            this.username = username;
            this.available = available;
        }
    }
}
