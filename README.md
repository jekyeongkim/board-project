# 🗒️ 게시판 서비스

> **진행 기간 : [ 24.05.28 ~ 24.06.27(1달) ]**

### ☀️ 목차

1. [프로그램 주요 기능](#프로그램-주요-기능)
    + [회원가입](#1-회원가입)
    + [로그인](#2-로그인)
    + [게시글 관리 (작성 / 수정 / 목록조회 / 삭제)](#3-게시글-관리)
    + [댓글 관리 (작성 / 수정 / 삭제)](#4-댓글-관리)
    + [좋아요](#5-좋아요)
    + [조회수](#6-조회수)
2. [추가 구현하고 싶은 기능들](#추가-구현하고-싶은-기능들)
3. [사용한 기술 스택 및 라이브러리](#사용한-기술-스택-및-라이브러리)
4. [ERD 데이터 모델링](#erd-데이터-모델링)
5. [Trouble Shooting](#trouble-shooting)
6. [프로젝트를 하며 느낀 점](#프로젝트를-하며-느낀-점)

## [프로그램 주요 기능]

### 1. 회원가입

- [x] 아이디, 비밀번호, 이메일를 입력해 회원가입 할 수 있다.
- [x] 아이디와 이메일은 중복 불가하다.
- [x] 카카오 OAuth2로 회원가입이 가능하다. 

### 2. 로그인

- [x] 회원가입 시 사용한 아이디와 비밀번호로 로그인 할 수 있다.
- [x] 카카오 OAuth2로 로그인이 가능하다. 
- [x] 회원가입과 로그인, 게시글 조회를 제외한 게시판 서비스의 모든 기능은 로그인 후 사용 가능하다.

### 3. 게시글 관리 (작성 / 수정 / 목록조회 / 삭제)

- [x] 게시글을 작성, 수정, 목록조회, 삭제 할 수 있다.
- [x] 다른 회원이 작성한 게시글은 수정 권한이 없다.

### 4. 댓글 관리 (작성 / 수정 / 삭제)

- [x] 댓글을 작성, 수정, 삭제 할 수 있다.
- [x] 다른 회원이 작성한 댓글은 수정 권한이 없다.

### 5. 좋아요

- [x] 게시글과 댓글에 좋아요를 누를 수 있으며 게시글과 댓글마다 한 번만 가능하다.
- [x] 좋아요 버튼을 누르면 "정말 삭제하시겠습니까?" 라는 문구가 뜨며 확인을 누르면 좋아요 수가 증가하며 반영된다.\
      한번 더 누르면 취소를 할 수 있다.
      

### 5. 조회수

- [ ] 게시글을 조회할 때마다 목록과 상세페이지에 조회수가 하나씩 증가하여 반영된다.


## [추가 구현하고 싶은 기능들]

> 추후 여유가 된다면 구현해보고 싶은 기능입니다.

- [ ] 회원가입 시 이메일 인증
- [ ] 회원 이미지 삽입

## [사용한 기술 스택 및 라이브러리]

<div align=center> 

<img height="30" src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/MySql-4479A1?style=flat-square&logo=mysql&logoColor=white"/>
<br/>
<img height="30" src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/GitHub-black?style=flat-square&logo=GitHub&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazon-aws&logoColor=white"/>

</div>

- 라이브러리 : Spring Web, Spring Data JPA, Lombok, Spring Security, Swagger

## [ERD 데이터 모델링]

![img](./docs/ERD%200730.png)

## 프로젝트 구조

```
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── board_project
│   │               ├── BoardProjectApplication.java
│   │               ├── auth
│   │               │   └── UserRole.java
│   │               ├── config
│   │               │   └── SecurityConfig.java
│   │               ├── controller
│   │               │   ├── CommentController.java
│   │               │   ├── HomeController.java
│   │               │   ├── PostController.java
│   │               │   └── UserController.java
│   │               ├── dto
│   │               │   ├── CommentRequest.java
│   │               │   ├── PostRequest.java
│   │               │   └── UserAccountRequest.java
│   │               ├── entity
│   │               │   ├── Comment.java
│   │               │   ├── Post.java
│   │               │   └── SiteUser.java
│   │               ├── repository
│   │               │   ├── CommentRepository.java
│   │               │   ├── PostRepository.java
│   │               │   └── UserRepository.java
│   │               ├── security
│   │               │   ├── CustomOAuth2User.java
│   │               │   ├── CustomOAuth2UserService.java
│   │               │   └── UserSecurityService.java
│   │               ├── service
│   │               │   ├── CommentService.java
│   │               │   ├── PostService.java
│   │               │   └── UserService.java
│   │               ├── swagger
│   │               │   └── SwaggerConfig.java
│   │               └── util
│   │                   ├── DataNotFoundException.java
│   │                   ├── OAuth2TypeMatchNotFoundException.java
│   │                   └── RepositoryUtil.java
│   └── resources
│       ├── application.properties
│       ├── static
│       │   ├── images
│       │   │   ├── comment-icon.png
│       │   │   ├── eye-icon.png
│       │   │   └── kakao-login.png
│       │   └── style.css
│       └── templates
│           ├── comment_form.html
│           ├── form_errors.html
│           ├── header.html
│           ├── join_form.html
│           ├── layout.html
│           ├── login_form.html
│           ├── post_detail.html
│           ├── post_list.html
│           └── write_form.html
└── test
    └── java
        └── com
            └── example
                └── board_project
                    ├── BoardProjectApplicationTests.java
                    ├── repository
                    │   ├── CommentRepositoryTests.java
                    │   └── PostRepositoryTests.java
                    └── service
                        └── UserServiceTests.java
```

## api docs

## Installation
