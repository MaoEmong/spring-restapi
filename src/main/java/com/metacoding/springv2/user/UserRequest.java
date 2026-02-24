package com.metacoding.springv2.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class UsernameCheckDTO {
        // rule1 반영: 유저네임 관련 요청 DTO 자리 (향후 확장용)
        private String username;
    }
}
