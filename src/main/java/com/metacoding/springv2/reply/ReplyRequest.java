package com.metacoding.springv2.reply;

import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.user.User;

import lombok.Data;

/**
 * 댓글 관련 요청 데이터를 담는 DTO들을 관리하는 클래스입니다.
 */
public class ReplyRequest {

    /**
     * 댓글 작성 시 요청 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class SaveDTO {
        private String comment;
        private Integer boardId;

        public Reply toEntity(User user, Board board) {
            return Reply.builder()
                    .comment(this.comment)
                    .user(user)
                    .board(board)
                    .build();
        }
    }

    /**
     * 댓글 수정 시 요청 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class UpdateDTO {
        private String comment;
    }
}
