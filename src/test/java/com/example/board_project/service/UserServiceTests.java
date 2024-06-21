package com.example.board_project.service;

import com.example.board_project.repository.CommentRepository;
import com.example.board_project.repository.CommentRepositoryTests;
import com.example.board_project.repository.PostRepository;
import com.example.board_project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    private void clearData() {
        clearData(userRepository, commentRepository, postRepository);
    }

    private void clearData(UserRepository userRepository,
                           CommentRepository commentRepository,
                           PostRepository postRepository) {
        CommentRepositoryTests.clearData(commentRepository, postRepository, userRepository);
        userRepository.deleteAll();
        userRepository.truncateTable();
    }

    private void createSampleData() {
        createSampleData(userService);
    }

    private void createSampleData(UserService userService) {
        userService.create("admin", "1234", "admin@email.com");
        userService.create("user1", "1234", "user1@email.com");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void 회원가입_테스트() {
        userService.create("user2", "1234", "user2@email.com");
    }
}
