package com.metacoding.springv2.love;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.board.BoardRepository;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * '좋아요' 기능을 처리하는 비즈니스 로직을 담당합니다.
 * rule1.md 규칙에 따라 트랜잭션과 비즈니스 로직을 관리합니다.
 */
@RequiredArgsConstructor
@Service
public class LoveService {

    private final LoveRepository loveRepository;
    private final BoardRepository boardRepository;

    /**
     * '좋아요' 토글 기능입니다.
     * 이미 좋아요를 눌렀으면 삭제하고 null을 반환하며, 
     * 누르지 않았으면 좋아요를 생성하고 DTO를 반환합니다.
     */
    @Transactional
    public LoveResponse.DTO toggle(LoveRequest.SaveDTO reqDTO, User user) {
        // 1. 게시글 존재 여부 확인
        Board board = boardRepository.findById(reqDTO.getBoardId())
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        // 2. 이미 해당 유저가 해당 게시글에 좋아요를 눌렀는지 확인
        Optional<Love> loveOp = loveRepository.findByBoardIdAndUserId(board.getId(), user.getId());

        if (loveOp.isPresent()) {
            // 이미 좋아요가 있으면 삭제 (Un-like)
            loveRepository.delete(loveOp.get());
            return null;
        } else {
            // 좋아요가 없으면 생성 (Like)
            Love love = Love.builder()
                    .board(board)
                    .user(user)
                    .build();
            Love savedLove = loveRepository.save(love);
            return new LoveResponse.DTO(savedLove);
        }
    }
}
