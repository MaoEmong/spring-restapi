package com.metacoding.springv2.love;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoveRepository extends JpaRepository<Love, Integer> {

    @Query("SELECT l FROM Love l WHERE l.board.id = :boardId AND l.user.id = :userId")
    Optional<Love> findByBoardIdAndUserId(@Param("boardId") Integer boardId, @Param("userId") Integer userId);
}
