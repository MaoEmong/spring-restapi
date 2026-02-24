package com.metacoding.springv2.user;

import lombok.Data;

public class UserResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String username;
        private String email;
        private String roles;

        // rule1 반영: /api/users/{id} 응답 DTO
        public DetailDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.roles = user.getRoles();
        }
    }
}
