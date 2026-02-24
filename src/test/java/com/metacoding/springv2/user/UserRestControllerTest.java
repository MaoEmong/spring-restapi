package com.metacoding.springv2.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

/**
 * 유저 정보 조회 API 통합 테스트입니다.
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("유저 상세 조회 성공 - 인증된 사용자")
    @Test
    public void findById_success_test() throws Exception {
        // given
        Integer id = 1;
        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/users/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.id").value(1))
                .andExpect(jsonPath("$.body.username").value("ssar"));
    }

    @DisplayName("유저 상세 조회 실패 - 인증되지 않은 사용자")
    @Test
    public void findById_fail_unauthorized_test() throws Exception {
        // given
        Integer id = 1;

        // when (토큰 없이 요청)
        ResultActions resultActions = mvc.perform(get("/api/users/" + id));

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.msg").value("Login required"));
    }
}
