package com.metacoding.springv2.love;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * '좋아요' 기능을 제공하는 REST 컨트롤러입니다.
 * AuthController와 동일한 공통 응답 스타일을 따릅니다.
 */
@RequiredArgsConstructor
@RestController
public class LoveController {

    private final LoveService loveService;

    /**
     * '좋아요' 토글 엔드포인트입니다.
     * /api/loves 주소를 통해 게시글의 좋아요를 누르거나 취소합니다.
     * rule1.md 에 따라 인증된 유저 정보를 SecurityContext에서 가져와 사용합니다.
     */
    @PostMapping("/api/loves")
    public ResponseEntity<?> toggle(@RequestBody LoveRequest.SaveDTO reqDTO) {
        // SecurityContext에서 현재 로그인된 유저 정보를 가져옵니다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 좋아요 토글 서비스를 실행하고 결과를 반환합니다.
        var respDTO = loveService.toggle(reqDTO, user);
        return Resp.ok(respDTO);
    }
}
