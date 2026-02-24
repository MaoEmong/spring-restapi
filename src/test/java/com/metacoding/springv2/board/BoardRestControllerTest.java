package com.metacoding.springv2.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.metacoding.springv2.reply.ReplyRepository;

/**
 * 게시글 조회 API 통합 테스트입니다.
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReplyRepository replyRepository;

    @DisplayName("게시글 작성 성공 - 인증된 사용자")
    @Test
    public void save_success_test() throws Exception {
        // given
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("new title");
        reqDTO.setContent("new content");
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/boards")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.title").value("new title"))
                .andExpect(jsonPath("$.body.content").value("new content"));
    }

    @DisplayName("게시글 수정 성공 - 작성자 본인")
    @Test
    public void update_success_test() throws Exception {
        // given
        Integer id = 1; // data.sql에서 ssar(id=1)이 작성한 글
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO();
        reqDTO.setTitle("updated title");
        reqDTO.setContent("updated content");
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(put("/api/boards/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.title").value("updated title"))
                .andExpect(jsonPath("$.body.content").value("updated content"));
    }

    @DisplayName("게시글 수정 실패 - 권한 없음 (작성자가 아님)")
    @Test
    public void update_fail_forbidden_test() throws Exception {
        // given
        Integer id = 1; // data.sql에서 ssar(id=1)이 작성한 글
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO();
        reqDTO.setTitle("updated title");
        reqDTO.setContent("updated content");
        String requestBody = om.writeValueAsString(reqDTO);

        User user = User.builder().id(2).username("cos").roles("USER").build(); // 다른 사용자
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(put("/api/boards/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.msg").value("게시글 수정 권한이 없습니다."));
    }

    @DisplayName("게시글 삭제 성공 - 작성자 본인 및 연관 댓글 삭제 확인")
    @Test
    public void delete_success_test() throws Exception {
        // given (4번 게시글에는 댓글 1, 2, 3번이 달려 있음)
        Integer id = 4; 
        User user = User.builder().id(2).username("cos").roles("USER,ADMIN").build(); // 작성자 cos
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/boards/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        // cascade 삭제 확인: 댓글 1, 2, 3번이 모두 존재하지 않아야 함
        assert(replyRepository.findById(1).isEmpty());
        assert(replyRepository.findById(2).isEmpty());
        assert(replyRepository.findById(3).isEmpty());
    }

    @DisplayName("게시글 목록 조회 성공 - 인증된 사용자")
    @Test
    public void findAll_success_test() throws Exception {
        // given (인증된 사용자 설정)
        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/boards")
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body").isArray()) // 리스트 응답 확인
                .andExpect(jsonPath("$.body[0].id").exists())
                .andExpect(jsonPath("$.body[0].title").exists())
                .andExpect(jsonPath("$.body[0].content").exists());
    }

    @DisplayName("게시글 목록 조회 실패 - 인증되지 않은 사용자")
    @Test
    public void findAll_fail_unauthorized_test() throws Exception {
        // given (토큰 없음)

        // when
        ResultActions resultActions = mvc.perform(get("/api/boards"));

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.msg").value("Login required"));
    }

    @DisplayName("게시글 상세 조회 성공 - 인증된 사용자 (댓글 포함)")
    @Test
    public void findById_success_test() throws Exception {
        // given (4번 게시글에는 댓글이 있음)
        Integer id = 4;
        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/boards/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.body.id").value(4))
                .andExpect(jsonPath("$.body.title").exists())
                .andExpect(jsonPath("$.body.content").exists())
                .andExpect(jsonPath("$.body.userId").exists())
                .andExpect(jsonPath("$.body.username").exists())
                .andExpect(jsonPath("$.body.replies").isArray()) // 댓글 리스트 확인
                .andExpect(jsonPath("$.body.replies[0].comment").exists())
                .andExpect(jsonPath("$.body.replies[0].username").exists());
    }

    @DisplayName("게시글 상세 조회 실패 - 존재하지 않는 게시글")
    @Test
    public void findById_fail_notfound_test() throws Exception {
        // given
        Integer id = 999;
        User user = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/boards/" + id)
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + jwt));

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.msg").value("해당 게시글을 찾을 수 없습니다. id: 999"));
    }
}
