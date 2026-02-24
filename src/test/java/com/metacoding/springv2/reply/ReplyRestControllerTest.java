package com.metacoding.springv2.reply;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * 댓글 관련 API 통합 테스트입니다.
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReplyRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @DisplayName("댓글 작성 성공 - 인증된 사용자")
    @Test
    public void save_success_test() throws Exception {
        // given
        ReplyRequest.SaveDTO reqDTO = new ReplyRequest.SaveDTO();
        reqDTO.setComment("new comment");
        reqDTO.setBoardId(1);
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/replies")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.comment").value("new comment"))
                .andExpect(jsonPath("$.body.boardId").value(1))
                .andExpect(jsonPath("$.body.username").value("ssar"));
    }

    @DisplayName("댓글 수정 성공 - 작성자 본인")
    @Test
    public void update_success_test() throws Exception {
        // given
        Integer id = 1; // data.sql에서 cos(id=2)가 작성한 댓글(id=1)
        ReplyRequest.UpdateDTO reqDTO = new ReplyRequest.UpdateDTO();
        reqDTO.setComment("updated comment");
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(2).username("cos").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(put("/api/replies/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.comment").value("updated comment"))
                .andExpect(jsonPath("$.body.username").value("cos"));
    }

    @DisplayName("댓글 수정 실패 - 권한 없음 (작성자가 아님)")
    @Test
    public void update_fail_forbidden_test() throws Exception {
        // given
        Integer id = 1; // data.sql에서 cos(id=2)가 작성한 댓글(id=1)
        ReplyRequest.UpdateDTO reqDTO = new ReplyRequest.UpdateDTO();
        reqDTO.setComment("updated comment");
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(put("/api/replies/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.msg").value("댓글 수정 권한이 없습니다."));
    }


    @DisplayName("댓글 삭제 성공 - 작성자 본인")
    @Test
    public void delete_success_test() throws Exception {
        // given
        Integer id = 1; // data.sql에서 cos(id=2)가 작성한 댓글(id=1)
        User user = User.builder().id(2).username("cos").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/replies/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @DisplayName("댓글 삭제 실패 - 권한 없음 (작성자가 아님)")
    @Test
    public void delete_fail_forbidden_test() throws Exception {
        // given
        Integer id = 1; // data.sql에서 cos(id=2)가 작성한 댓글(id=1)
        User user = User.builder().id(1).username("ssar").roles("USER").build(); // 다른 사용자
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/replies/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.msg").value("댓글 삭제 권한이 없습니다."));
    }
}
