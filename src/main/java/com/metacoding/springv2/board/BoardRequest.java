package com.metacoding.springv2.board;

import com.metacoding.springv2.user.User;
import lombok.Data;

/**
 * 게시글과 관련된 요청 데이터를 담는 DTO들을 관리하는 클래스입니다.
 */
public class BoardRequest {

    /**
     * 게시글 작성 시 요청 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class SaveDTO {
        private String title;
        private String content;

        // DTO를 엔티티로 변환하는 메서드입니다.
        public Board toEntity(User user) {
            return Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .user(user)
                    .build();
        }
    }

    /**
     * 게시글 수정 시 요청 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
    }
}
