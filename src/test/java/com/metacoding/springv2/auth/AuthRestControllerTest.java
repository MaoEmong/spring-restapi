package com.metacoding.springv2.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;

/**
 * 인증 관련 API 통합 테스트입니다.
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("로그아웃 성공")
    @Test
    public void logout_success_test() throws Exception {
        // given
        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/logout")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @DisplayName("유저네임 중복 체크 성공 - 사용 가능")
    @Test
    public void checkUsername_success_test() throws Exception {
        // given
        String username = "newuser";

        // when
        ResultActions resultActions = mvc.perform(get("/username-same-check")
                .param("username", username));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.body.available").value(true)); // 중복되지 않음
    }

    @DisplayName("유저네임 중복 체크 실패 - 이미 존재함")
    @Test
    public void checkUsername_fail_test() throws Exception {
        // given (data.sql에 ssar 사용자가 있다고 가정)
        String username = "ssar";

        // when
        ResultActions resultActions = mvc.perform(get("/username-same-check")
                .param("username", username));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.available").value(false)); // 중복됨
    }
}
