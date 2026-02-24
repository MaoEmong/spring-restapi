package com.metacoding.springv2.board;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 게시글 관련 REST 엔드포인트를 정의하는 컨트롤러입니다.
 * AuthController의 패턴을 따라 일관된 응답 형식을 제공합니다.
 */
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    /**
     * 특정 게시글을 수정하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/boards/{id} 형식의 REST 주소를 사용하며, 인증된 사용자 정보를 포함합니다.
     */
    @PutMapping("/api/boards/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody BoardRequest.UpdateDTO reqDTO) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 게시글을 수정하고 결과를 반환합니다.
        var respDTO = boardService.update(id, reqDTO, user);
        return Resp.ok(respDTO);
    }

    /**
     * 특정 게시글을 삭제하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/boards/{id} 형식의 REST 주소를 사용하며, 인증된 사용자 정보를 포함합니다.
     */
    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 게시글을 삭제하고 성공 결과를 반환합니다.
        boardService.delete(id, user);
        return Resp.ok(null);
    }

    /**
     * 게시글을 작성하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/boards 형식의 REST 주소를 사용하며, 인증된 사용자 정보를 포함합니다.
     */
    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@RequestBody BoardRequest.SaveDTO reqDTO) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 게시글을 저장하고 결과를 반환합니다.
        var respDTO = boardService.save(reqDTO, user);
        return Resp.ok(respDTO);
    }

    /**
     * 모든 게시글 목록을 조회하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/boards 형식의 REST 주소를 사용하며, Resp.ok() 형식을 따릅니다.
     */
    @GetMapping("/api/boards")
    public ResponseEntity<?> findAll() {
        // 모든 게시글을 조회하여 목록 DTO 리스트를 반환합니다.
        var respDTO = boardService.findAll();
        return Resp.ok(respDTO);
    }

    /**
     * 특정 게시글의 상세 정보를 조회하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/boards/{id} 형식의 REST 주소를 사용합니다.
     */
    @GetMapping("/api/boards/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        // 특정 게시글의 상세 정보를 조회하여 DetailDTO로 반환합니다.
        var respDTO = boardService.findById(id);
        return Resp.ok(respDTO);
    }
}
