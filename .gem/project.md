# 프로젝트 분석: spring-restapi

## 1. 개요

이 저장소는 커뮤니티형 백엔드를 목표로 한 Spring Boot REST API 프로젝트입니다.  
현재는 회원가입/로그인 중심의 인증 흐름과 JWT 기반 인가 구조가 핵심으로 구현되어 있습니다.

- 런타임: Spring Boot 3.5.5, Java 21, Gradle
- 주요 라이브러리: Spring Web, Spring Security, Spring Data JPA, Validation, H2, Java JWT(Auth0)
- 기본 프로필: `dev` (`src/main/resources/application.properties`)

## 2. 현재 구현 상태

### 구현 완료

- 인증 API
- `POST /join`: 회원가입(비밀번호 BCrypt 해싱)
- `POST /login`: 아이디/비밀번호 검증 후 JWT 발급
- `GET /health`: 헬스 체크
- 보안 설정
- Stateless 세션 정책
- JWT 인가 필터 연결(`JwtAuthorizationFilter`)
- URL 인가 규칙
- `/api/**`: 인증 필요
- `/admin/**`: `ROLE_ADMIN` 필요
- 공통 응답/예외 처리
- `Resp` 응답 포맷(`status`, `msg`, `body`)
- `GlobalExceptionHandler`를 통한 예외 매핑
- `GlobalValidationHandler`를 통한 POST/PUT 유효성 검증
- 도메인/영속성 매핑
- `User`, `Board`, `Reply` JPA 엔티티
- `UserRepository.findByUsername(...)`
- 초기 데이터(`src/main/resources/db/data.sql`)

### 부분 구현 / 플레이스홀더

- `BoardController`, `BoardService`, `BoardRepository`는 파일은 있으나 실동작 로직 미구현
- `ReplyController`, `ReplyService`, `ReplyRepository`는 파일은 있으나 실동작 로직 미구현
- `UserController`, `UserService`는 사실상 비어 있는 상태
- `AdminService`는 비어 있고, `/admin/test` 엔드포인트만 존재

## 3. 패키지 구조

- `com.metacoding.springv2.auth`: 회원가입/로그인 요청·응답 DTO, 서비스, 컨트롤러
- `com.metacoding.springv2.user`: 사용자 엔티티/리포지토리 및 플레이스홀더
- `com.metacoding.springv2.board`: 게시글 엔티티 및 플레이스홀더
- `com.metacoding.springv2.reply`: 댓글 엔티티 및 플레이스홀더
- `com.metacoding.springv2.admin`: 관리자 엔드포인트 및 플레이스홀더 서비스
- `com.metacoding.springv2._core`
- `config`: 보안/필터 설정
- `filter`: JWT/CORS 필터
- `handler`: 전역 예외/검증 처리
- `util`: JWT/응답 유틸리티

## 4. 데이터 및 프로필 설정

### dev 프로필 (`application-dev.properties`)

- H2 메모리 DB 사용 (`jdbc:h2:mem:test`)
- `spring.jpa.hibernate.ddl-auto=update`
- `classpath:db/data.sql` 초기 데이터 로딩
- H2 콘솔 활성화

### prod 프로필 (`application-prod.properties`)

- MySQL 환경변수 기반 연결
- `spring.jpa.hibernate.ddl-auto=none`
- H2 콘솔 비활성화

## 5. 보안/인증 메모

- 로그인 성공 시 JWT를 발급합니다.
- 인가 필터에서 토큰을 해석해 `SecurityContext`를 설정합니다.
- `User` 엔티티는 `UserDetails`를 구현하며, `roles` CSV를 `ROLE_*` 권한으로 변환합니다.
- 일부 메서드명/문자열에 인코딩 깨짐 흔적이 있어, 유지보수를 위해 식별자 및 문자열 표준화가 필요합니다.

## 6. 리스크 및 갭

- 핵심 기능인 게시글/댓글 CRUD가 아직 연결되지 않았습니다.
- 현재 공개 API 범위가 매우 작습니다 (`/join`, `/login`, `/health`, `/admin/test`).
- `FilterConfig`의 CORS 필터 등록이 주석 처리되어 있습니다.
- 테스트 코드가 거의 없거나 부재한 상태입니다.

## 7. 권장 다음 작업

1. 게시글/댓글/사용자 영역의 서비스-컨트롤러-리포지토리 흐름을 실제 동작하도록 구현
2. 인증/인가 규칙 중심의 통합 테스트 추가
3. 인코딩 깨짐 방지를 위한 메서드명/문자열 정리(ASCII 중심 또는 UTF-8 일관성 확보)
4. CORS 처리 전략을 하나로 통일하고 미사용 코드 제거
5. 운영 DB 마이그레이션 전략(Flyway/Liquibase) 도입

