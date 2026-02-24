package com.metacoding.springv2.love;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;

/**
 * '좋아요' API 통합 테스트입니다.
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LoveRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @DisplayName("좋아요 토글 성공 - 좋아요 추가")
    @Test
    public void toggle_like_success_test() throws Exception {
        // given
        LoveRequest.SaveDTO reqDTO = new LoveRequest.SaveDTO();
        reqDTO.setBoardId(1);
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/loves")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.boardId").value(1))
                .andExpect(jsonPath("$.body.userId").value(1));
    }

    @DisplayName("좋아요 토글 성공 - 좋아요 취소")
    @Test
    public void toggle_unlike_success_test() throws Exception {
        // given
        LoveRequest.SaveDTO reqDTO = new LoveRequest.SaveDTO();
        reqDTO.setBoardId(1);
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // 첫 번째 호출: 좋아요 추가
        mvc.perform(post("/api/loves")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // when: 두 번째 호출 (좋아요 취소)
        ResultActions resultActions = mvc.perform(post("/api/loves")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body").isEmpty()); // 취소 시 null 반환 확인
    }

    @DisplayName("좋아요 실패 - 존재하지 않는 게시글")
    @Test
    public void toggle_fail_notfound_test() throws Exception {
        // given
        LoveRequest.SaveDTO reqDTO = new LoveRequest.SaveDTO();
        reqDTO.setBoardId(999);
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/loves")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.msg").value("게시글을 찾을 수 없습니다."));
    }

    @DisplayName("좋아요 실패 - 인증되지 않은 사용자")
    @Test
    public void toggle_fail_unauthorized_test() throws Exception {
        // given
        LoveRequest.SaveDTO reqDTO = new LoveRequest.SaveDTO();
        reqDTO.setBoardId(1);
        String requestBody = om.writeValueAsString(reqDTO);

        // when (토큰 없음)
        ResultActions resultActions = mvc.perform(post("/api/loves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.msg").value("Login required"));
    }
}
