# Agent1 작업 로그 (Work Log)

## 프로젝트: spring-restapi
**작성자:** Gemini CLI (Agent1)
**마지막 업데이트:** 2026-02-23

---

## [2026-02-23] 작업 내역

### 1. 게시글 목록 기능 구현 (Task 3)
- **내용:** 모든 게시글을 조회하여 DTO 리스트로 반환하는 기능 추가.
- **변경 사항:**
  - `BoardRepository`: `JpaRepository` 상속 추가.
  - `BoardResponse.ListDTO`: `id`, `title`, `content` 필드를 포함하는 DTO 정의 (@Data 사용).
  - `BoardService.findAll()`: 엔티티를 DTO로 변환하여 리스트 반환.
  - `BoardController.findAll()`: `GET /api/boards` 엔드포인트 구현.

### 2. 불필요한 보안 설정 파일 제거 (Rule 준수)
- **내용:** `SecurityConfig`에 CORS 설정이 통합됨에 따라 중복된 파일 삭제.
- **삭제 항목:**
  - `src/main/java/com/metacoding/springv2/_core/config/FilterConfig.java`
  - `src/main/java/com/metacoding/springv2/_core/filter/CorsFilter.java`

### 3. 게시글 상세보기 및 쿼리 최적화 (Task 4 & Performance)
- **내용:** 게시글 상세 정보를 조회하며, N+1 문제를 해결하기 위해 Fetch Join 적용.
- **변경 사항:**
  - `BoardRepository.mFindDetailById()`: `JOIN FETCH b.user`를 사용하여 한 번의 쿼리로 작성자 정보까지 조회.
  - `BoardService.findById()`: 최적화된 리포지토리 메서드 호출.
  - `BoardController.findById()`: `GET /api/boards/{id}` 엔드포인트 구현.

### 4. 게시글 상세보기 리팩토링 (Task 5)
- **내용:** 응답 DTO를 평탄화(Flat)하여 내부 클래스 제거.
- **변경 사항:**
  - `BoardResponse.DetailDTO`: `(id, title, content, userId, username)` 구조로 변경.

### 5. 게시글 작성, 수정, 삭제 기능 (Task 6)
- **내용:** 게시글 CRUD의 나머지 기능 구현 및 권한 확인 추가.
- **변경 사항:**
  - `BoardRequest.SaveDTO`, `UpdateDTO` 정의.
  - `BoardResponse.SaveDTO`, `UpdateDTO` 정의.
  - `BoardService`: `save`, `update`, `delete` 로직 구현. (작성자 불일치 시 `Exception403` 처리)
  - `BoardController`: `POST /api/boards`, `PUT /api/boards/{id}`, `DELETE /api/boards/{id}` 구현.

### 6. 댓글 작성 및 삭제 기능 (Task 7)
- **내용:** 게시글에 댓글을 달고 본인의 댓글을 삭제하는 기능 추가.
- **변경 사항:**
  - `ReplyRepository`: `JpaRepository` 상속 추가.
  - `ReplyRequest.SaveDTO`, `ReplyResponse.DTO` 정의.
  - `ReplyService`: `save`, `delete` 로직 및 권한 확인 구현.
  - `ReplyController`: `POST /api/replies`, `DELETE /api/replies/{id}` 구현.

### 7. 로그아웃 기능 (Task 8)
- **내용:** 인증된 사용자의 로그아웃 처리.
- **변경 사항:**
  - `AuthController.logout()`: `POST /api/logout` 엔드포인트 구현. (JWT 방식에 맞춰 응답)

### 8. 통합 테스트 수행 및 검증
- **테스트 클래스 생성:**
  - `AuthRestControllerTest`: 중복 체크, 로그아웃 검증.
  - `UserRestControllerTest`: 유저 상세 조회 검증.
  - `BoardRestControllerTest`: 게시글 CRUD 전체 검증.
  - `ReplyRestControllerTest`: 댓글 작성/삭제 및 권한 검증.
- **결과:** 모든 테스트 통과 (BUILD SUCCESSFUL).

### 9. 댓글 보기 (게시글 상세보기에 포함 - Task 9)
- **내용:** 게시글 상세 조회 시 해당 게시글에 달린 모든 댓글과 작성자 정보를 함께 반환하도록 기능을 확장했습니다.
- **변경 사항:**
  - `BoardResponse.DetailDTO`: `List<ReplyDTO>` 필드를 추가하고 댓글 정보를 평탄화하여 포함시켰습니다.
  - `BoardRepository.mFindDetailById()`: 댓글(`replies`)과 댓글 작성자(`reply.user`)까지 한 번의 쿼리로 가져오도록 `FETCH JOIN`을 확장하여 N+1 문제를 방지했습니다.
  - `BoardRestControllerTest`: 상세 보기 응답에서 댓글 목록이 올바르게 반환되는지 검증하는 테스트를 추가했습니다.

### 10. 게시글 삭제 시 연관 댓글 자동 삭제 (Cascade REMOVE - Task 10)
- **내용:** 게시글 삭제 시 해당 게시글에 달린 모든 댓글이 함께 삭제되도록 설정 및 검증을 완료했습니다.
- **변경 사항:**
  - `Board` 엔티티의 `replies` 필드에 `cascade = CascadeType.REMOVE` 설정이 되어 있음을 확인했습니다.
  - `BoardRestControllerTest.delete_success_test()`를 확장하여, 게시글 삭제 후 연관된 댓글들이 DB에서 사라졌는지 `ReplyRepository`를 통해 검증했습니다.

### 11. 댓글 수정 기능 구현 (Task 11)
- **내용:** 댓글을 수정할 수 있는 기능을 추가하고 권한 검증을 적용했습니다.
- **변경 사항:**
  - `Reply` 엔티티: `update` 메서드 추가.
  - `ReplyRequest.UpdateDTO`, `ReplyResponse.UpdateDTO` 추가.
  - `ReplyService.update()`: 작성자 본인 확인 후 수정 로직 구현.
  - `ReplyController.update()`: `PUT /api/replies/{id}` 엔드포인트 추가.
  - `ReplyRestControllerTest`: 댓글 수정 성공 및 실패(권한 없음) 테스트 추가.

### 12. 프로젝트 재점검 및 테스트 (Task 12)
- **내용:** 전체 기능 및 코드 품질 점검.
- **검증:** `gradlew test` 전체 통과 확인.
- **확인 항목:**
  - 불필요한 파일/코드 삭제 여부 (완료)
  - DTO 규칙 준수 (완료)
  - 보안 설정 (CORS, 권한 검증) 확인 (완료)

### 13. 프로젝트 문서화 (.book 폴더) (Task 13)
- **내용:** 프로젝트 구조 및 API 명세를 정리한 문서를 생성했습니다.
- **생성 파일:**
  - `.book/overview.md`: 프로젝트 개요 및 기술 스택
  - `.book/api-docs.md`: API 목록 및 설명
  - `.book/db-schema.md`: 데이터베이스 스키마 정보

---
## 향후 계획
- 게시글에 받은 좋아요 수가 표시되도록 추가 (Task 15)
- 게시글 및 댓글 신고 기능 구현 (Task 16, 17)

## [2026-02-23] 작업 내역 추가

### 14. 게시글 좋아요 기능 구현 (Task 14)
- **내용:** 사용자가 게시글에 '좋아요'를 누르거나 취소할 수 있는 토글 기능을 추가했습니다.
- **변경 사항:**
  - `Love` 엔티티: `board_id`와 `user_id`의 복합 유니크 제약 조건을 가진 좋아요 엔티티 생성.
  - `LoveRepository`: 특정 게시글과 유저의 좋아요 여부를 확인하는 쿼리 추가.
  - `LoveService.toggle()`: 이미 좋아요가 존재하면 삭제(취소), 없으면 생성(추가)하는 비즈니스 로직 구현.
  - `LoveController`: `POST /api/loves` 엔드포인트 구현 (인증 필수).
  - `LoveRestControllerTest`: 좋아요 추가, 취소, 예외 상황(404, 401)에 대한 통합 테스트 작성 및 검증 완료.
- **검증 결과:** `LoveRestControllerTest` 포함 전체 테스트 통과 (BUILD SUCCESSFUL).
