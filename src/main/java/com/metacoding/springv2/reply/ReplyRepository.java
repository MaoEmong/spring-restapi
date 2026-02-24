package com.metacoding.springv2.reply;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 댓글 엔티티에 대한 데이터 액세스를 담당하는 JPA 리포지토리입니다.
 */
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    
}
