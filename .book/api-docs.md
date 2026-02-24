# API 명세

## 1. 인증 (Auth)
- `POST /login`: 로그인, JWT 발급
- `POST /join`: 회원가입
- `GET /username-same-check`: 아이디 중복 확인
- `POST /api/logout`: 로그아웃 (인증 필요)

## 2. 사용자 (User)
- `GET /api/users/{id}`: 사용자 상세 조회 (인증 필요)

## 3. 게시글 (Board)
- `GET /api/boards`: 게시글 목록 조회 (인증 필요)
- `GET /api/boards/{id}`: 게시글 상세 조회 (댓글 목록 포함) (인증 필요)
- `POST /api/boards`: 게시글 작성 (인증 필요)
- `PUT /api/boards/{id}`: 게시글 수정 (작성자만 가능) (인증 필요)
- `DELETE /api/boards/{id}`: 게시글 삭제 (작성자만 가능) (인증 필요)

## 4. 댓글 (Reply)
- `POST /api/replies`: 댓글 작성 (인증 필요)
- `PUT /api/replies/{id}`: 댓글 수정 (작성자만 가능) (인증 필요)
- `DELETE /api/replies/{id}`: 댓글 삭제 (작성자만 가능) (인증 필요)

## 5. 기타
- `GET /health`: 헬스 체크
