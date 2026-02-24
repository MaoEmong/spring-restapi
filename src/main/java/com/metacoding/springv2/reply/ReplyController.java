package com.metacoding.springv2.reply;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;

/**
 * 댓글 관련 REST 엔드포인트를 정의하는 컨트롤러입니다.
 * AuthController의 패턴을 따라 일관된 응답 형식을 제공합니다.
 */
@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글을 수정하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/replies/{id} 형식의 REST 주소를 사용하며, 인증된 사용자 정보를 포함합니다.
     */
    @PutMapping("/api/replies/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ReplyRequest.UpdateDTO reqDTO) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 댓글을 수정하고 결과를 반환합니다.
        var respDTO = replyService.update(id, reqDTO, user);
        return Resp.ok(respDTO);
    }

    /**
     * 댓글을 작성하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/replies 형식의 REST 주소를 사용하며, 인증된 사용자 정보를 포함합니다.
     */
    @PostMapping("/api/replies")
    public ResponseEntity<?> save(@RequestBody ReplyRequest.SaveDTO reqDTO) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 댓글을 저장하고 결과를 반환합니다.
        var respDTO = replyService.save(reqDTO, user);
        return Resp.ok(respDTO);
    }

    /**
     * 특정 댓글을 삭제하는 엔드포인트입니다.
     * rule1.md 규칙에 따라 /api/replies/{id} 형식의 REST 주소를 사용하며, 인증된 사용자 정보를 포함합니다.
     */
    @DeleteMapping("/api/replies/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 댓글을 삭제하고 성공 결과를 반환합니다.
        replyService.delete(id, user);
        return Resp.ok(null);
    }
}
