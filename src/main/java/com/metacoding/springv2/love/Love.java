package com.metacoding.springv2.love;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 게시글 '좋아요' 기능을 담당하는 엔티티입니다.
 * 유저와 게시글의 다대일 관계를 가집니다.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "love_tb", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"board_id", "user_id"})
})
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Love(Integer id, Board board, User user, Timestamp createdAt) {
        this.id = id;
        this.board = board;
        this.user = user;
        this.createdAt = createdAt;
    }
}
