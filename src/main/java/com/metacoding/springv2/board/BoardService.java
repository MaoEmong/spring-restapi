package com.metacoding.springv2.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 게시글 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 특정 게시글을 수정합니다.
     * rule1.md 반영: 게시글을 조회하고 작성자 권한을 확인한 뒤 필드를 업데이트합니다.
     */
    @Transactional
    public BoardResponse.UpdateDTO update(Integer id, BoardRequest.UpdateDTO reqDTO, User user) {
        // 수정할 게시글을 조회하고, 존재하지 않을 경우 404 예외를 던집니다.
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다. id: " + id));

        // 작성자 권한을 확인합니다.
        if (!board.getUser().getId().equals(user.getId())) {
            throw new Exception403("게시글 수정 권한이 없습니다.");
        }

        // 게시글 정보를 업데이트하고, 변경된 내용을 DTO로 변환하여 반환합니다.
        board.update(reqDTO.getTitle(), reqDTO.getContent());
        return new BoardResponse.UpdateDTO(board);
    }

    /**
     * 특정 게시글을 삭제합니다.
     * rule1.md 반영: 게시글을 조회하고 작성자 권한을 확인한 뒤 삭제를 수행합니다.
     */
    @Transactional
    public void delete(Integer id, User user) {
        // 삭제할 게시글을 조회하고, 존재하지 않을 경우 404 예외를 던집니다.
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다. id: " + id));

        // 작성자 권한을 확인합니다.
        if (!board.getUser().getId().equals(user.getId())) {
            throw new Exception403("게시글 삭제 권한이 없습니다.");
        }

        // 게시글을 리포지토리에서 삭제합니다.
        boardRepository.delete(board);
    }

    /**
     * 게시글을 저장합니다.
     * rule1.md 반영: 요청 DTO와 인증된 유저 정보를 받아 처리합니다.
     */
    @Transactional
    public BoardResponse.SaveDTO save(BoardRequest.SaveDTO reqDTO, User user) {
        // DTO를 엔티티로 변환하여 저장하고, 저장된 결과를 DTO로 변환하여 반환합니다.
        Board board = boardRepository.save(reqDTO.toEntity(user));
        return new BoardResponse.SaveDTO(board);
    }

    /**
     * 모든 게시글을 조회하여 목록 전용 DTO 리스트로 반환합니다.
     * readOnly = true를 통해 데이터 변경 없이 조회 성능을 최적화합니다.
     */
    @Transactional(readOnly = true)
    public List<BoardResponse.ListDTO> findAll() {
        // 모든 게시글을 리포지토리에서 조회한 후, 스트림을 이용해 DTO 리스트로 변환합니다.
        return boardRepository.findAll().stream()
                .map(board -> new BoardResponse.ListDTO(board))
                .collect(Collectors.toList());
    }

    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * rule1.md 반영: 존재하지 않을 경우 Exception404 예외를 발생시킵니다.
     * fetch join을 사용한 mFindDetailById를 통해 쿼리 횟수를 1회로 단축합니다.
     */
    @Transactional(readOnly = true)
    public BoardResponse.DetailDTO findById(Integer id) {
        // 게시글을 유저 정보와 함께 한 번에 조회하고, 존재하지 않을 경우 404 예외를 던집니다.
        Board board = boardRepository.mFindDetailById(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다. id: " + id));

        return new BoardResponse.DetailDTO(board);
    }
}
