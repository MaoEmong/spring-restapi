package com.metacoding.springv2.reply;

import lombok.Data;

/**
 * 댓글 관련 응답 DTO들을 관리하는 클래스입니다.
 */
public class ReplyResponse {

    /**
     * 댓글 작성 후 응답 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class DTO {
        private Integer id;
        private String comment;
        private Integer boardId;
        private String username;

        public DTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.boardId = reply.getBoard().getId();
            this.username = reply.getUser().getUsername();
        }
    }

    /**
     * 댓글 수정 후 응답 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class UpdateDTO {
        private Integer id;
        private String comment;
        private Integer boardId;
        private String username;

        public UpdateDTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.boardId = reply.getBoard().getId();
            this.username = reply.getUser().getUsername();
        }
    }
}
