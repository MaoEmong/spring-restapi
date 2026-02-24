package com.metacoding.springv2.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 게시글 엔티티에 대한 데이터 액세스를 담당하는 JPA 리포지토리입니다.
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {

    /**
     * 게시글과 연관된 유저, 그리고 댓글과 댓글 작성자 정보를 fetch join을 통해 한 번의 쿼리로 조회합니다.
     * 상세 보기 시 발생하는 N+1 문제를 해결합니다.
     */
    @Query("select distinct b from Board b join fetch b.user left join fetch b.replies r left join fetch r.user where b.id = :id")
    Optional<Board> mFindDetailById(@Param("id") Integer id);
}
