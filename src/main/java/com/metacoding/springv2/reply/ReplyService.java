package com.metacoding.springv2.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.board.BoardRepository;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 댓글 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    /**
     * 댓글을 저장합니다.
     * rule1.md 반영: 요청 DTO와 인증된 유저 정보를 받아 처리합니다.
     */
    @Transactional
    public ReplyResponse.DTO save(ReplyRequest.SaveDTO reqDTO, User user) {
        // 댓글을 달 게시글을 조회하고, 존재하지 않을 경우 404 예외를 던집니다.
        Board board = boardRepository.findById(reqDTO.getBoardId())
                .orElseThrow(() -> new Exception404("댓글을 달 게시글을 찾을 수 없습니다. id: " + reqDTO.getBoardId()));

        // 댓글을 엔티티로 변환하여 저장하고, 결과를 DTO로 반환합니다.
        Reply reply = replyRepository.save(reqDTO.toEntity(user, board));
        return new ReplyResponse.DTO(reply);
    }

    /**
     * 댓글을 삭제합니다.
     * rule1.md 반영: 댓글을 조회하고 작성자 권한을 확인한 뒤 삭제를 수행합니다.
     */
    @Transactional
    public void delete(Integer id, User user) {
        // 삭제할 댓글을 조회하고, 존재하지 않을 경우 404 예외를 던집니다.
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new Exception404("삭제할 댓글을 찾을 수 없습니다. id: " + id));

        // 작성자 권한을 확인합니다.
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new Exception403("댓글 삭제 권한이 없습니다.");
        }

        // 댓글을 리포지토리에서 삭제합니다.
        replyRepository.delete(reply);
    }

    /**
     * 댓글을 수정합니다.
     * rule1.md 반영: 댓글을 조회하고 작성자 권한을 확인한 뒤 수정합니다.
     */
    @Transactional
    public ReplyResponse.UpdateDTO update(Integer id, ReplyRequest.UpdateDTO reqDTO, User user) {
        // 수정할 댓글을 조회하고, 존재하지 않을 경우 404 예외를 던집니다.
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 댓글을 찾을 수 없습니다. id: " + id));

        // 작성자 권한을 확인합니다.
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new Exception403("댓글 수정 권한이 없습니다.");
        }

        // 댓글 내용을 업데이트하고, 결과를 DTO로 반환합니다.
        reply.update(reqDTO.getComment());
        return new ReplyResponse.UpdateDTO(reply);
    }
}
