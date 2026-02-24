package com.metacoding.springv2.board;

import java.util.List;
import java.util.stream.Collectors;

import com.metacoding.springv2.reply.Reply;

import lombok.Data;

/**
 * 게시글 조회와 관련된 응답 DTO들을 관리하는 클래스입니다.
 */
public class BoardResponse {

    /**
     * 게시글 목록 조회 시 각 게시글의 정보를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용하고, id, title, content를 필드로 포함합니다.
     */
    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String content;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    /**
     * 게시글 상세보기 시 게시글, 작성자 정보, 그리고 댓글 목록을 담는 DTO입니다.
     * 리팩토링: 필드를 평탄화하고 댓글 정보를 리스트로 포함합니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private List<ReplyDTO> replies; // 댓글 목록 필드

        public DetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            // 댓글 엔티티를 ReplyDTO 리스트로 변환하여 할당합니다.
            this.replies = board.getReplies().stream()
                    .map(ReplyDTO::new)
                    .collect(Collectors.toList());
        }

        /**
         * 게시글 상세보기 시 각 댓글의 정보를 담는 DTO입니다.
         * 평탄화된 구조를 따릅니다.
         */
        @Data
        public static class ReplyDTO {
            private Integer id;
            private String comment;
            private Integer userId;
            private String username;

            public ReplyDTO(Reply reply) {
                this.id = reply.getId();
                this.comment = reply.getComment();
                this.userId = reply.getUser().getId();
                this.username = reply.getUser().getUsername();
            }
        }
    }

    /**
     * 게시글 작성 후 응답 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class SaveDTO {
        private Integer id;
        private String title;
        private String content;

        public SaveDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    /**
     * 게시글 수정 후 응답 데이터를 담는 DTO입니다.
     * rule1.md 규칙에 따라 @Data를 사용합니다.
     */
    @Data
    public static class UpdateDTO {
        private Integer id;
        private String title;
        private String content;

        public UpdateDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
}
