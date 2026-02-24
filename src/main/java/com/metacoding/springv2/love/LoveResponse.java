package com.metacoding.springv2.love;

import java.sql.Timestamp;
import lombok.Data;

public class LoveResponse {

    /**
     * 좋아요 처리 후 응답 DTO입니다.
     * rule1.md 에 따라 필요한 필드만 포함합니다.
     */
    @Data
    public static class DTO {
        private Integer id;
        private Integer boardId;
        private Integer userId;
        private Timestamp createdAt;

        public DTO(Love love) {
            this.id = love.getId();
            this.boardId = love.getBoard().getId();
            this.userId = love.getUser().getId();
            this.createdAt = love.getCreatedAt();
        }
    }
}
