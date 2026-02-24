# 데이터베이스 구조 (Schema)

## 1. user_tb
- **id**: PK (INTEGER)
- **username**: 아이디 (VARCHAR(20), Unique)
- **password**: 비밀번호 (VARCHAR(60))
- **email**: 이메일 (VARCHAR(30))
- **roles**: 권한 (VARCHAR(255))
- **created_at**: 생성일 (TIMESTAMP)

## 2. board_tb
- **id**: PK (INTEGER)
- **title**: 제목 (VARCHAR(30))
- **content**: 내용 (VARCHAR(300))
- **user_id**: 작성자 (FK)
- **created_at**: 생성일 (TIMESTAMP)
- **OneToMany**: `reply_tb` (CascadeType.REMOVE)

## 3. reply_tb
- **id**: PK (INTEGER)
- **comment**: 내용 (VARCHAR(100))
- **user_id**: 작성자 (FK)
- **board_id**: 게시글 (FK)
- **created_at**: 생성일 (TIMESTAMP)

## 4. 연관 관계
- **Board** : **User** (N:1, ManyToOne, Lazy)
- **Reply** : **User** (N:1, ManyToOne, Lazy)
- **Reply** : **Board** (N:1, ManyToOne, Lazy)
