package com.example.board_project.repository;

import com.example.board_project.entity.Comment;
import com.example.board_project.entity.Post;
import com.example.board_project.entity.SiteUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository; // SiteUserRepository 추가

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    public void clearData() {
        clearData(commentRepository, postRepository, userRepository);
    }

    public static void clearData(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        PostRepositoryTests.clearData(postRepository, userRepository);
        commentRepository.deleteAll();
        commentRepository.truncateTable(); // RepositoryUtil의 default 메소드 실행
    }

    private void createSampleData() {
        PostRepositoryTests.createSampleData(postRepository, userRepository);

        Post post = postRepository.findById(1L).get();

        SiteUser author1 = new SiteUser();
        author1.setUsername("author1"); // 유저 이름 설정
        userRepository.save(author1);

        SiteUser author2 = new SiteUser();
        author2.setUsername("author2"); // 유저 이름 설정
        userRepository.save(author2);

        Comment comment1 = new Comment();
        comment1.setContent("1번 답변입니다.");
        comment1.setCreateDate(LocalDateTime.now());
        comment1.setAuthor(author1);
        post.addComment(comment1);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("2번 답변입니다.");
        comment2.setCreateDate(LocalDateTime.now());
        comment2.setAuthor(author2);
        post.addComment(comment2);
        commentRepository.save(comment2);

        postRepository.save(post);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("댓글 등록 테스트")
    void 댓글_등록_테스트() {
        Post post = postRepository.findById(1L).get();

        SiteUser author1 = userRepository.findByUsername("author1").orElseThrow();
        SiteUser author2 = userRepository.findByUsername("author2").orElseThrow();

        Comment comment1 = new Comment();
        comment1.setContent("1번 답변입니다.");
        comment1.setCreateDate(LocalDateTime.now());
        comment1.setAuthor(author1);
        post.addComment(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("2번 답변입니다.");
        comment2.setCreateDate(LocalDateTime.now());
        comment2.setAuthor(author2);
        post.addComment(comment2);

        postRepository.save(post);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("답변 조회 테스트")
    void 댓글_조회_테스트() {
        Comment comment = commentRepository.findById(1L).get();
        assertThat(comment.getContent()).isEqualTo("1번 답변입니다.");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("댓글로부터 게시글 가져오기 테스트")
    void 댓글로부터_게시글_가져오기_테스트() {
        Comment comment = commentRepository.findById(1L).get();
        Post post = comment.getPost();
        assertThat(post.getId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("게시글로부터 답변들 가져오기 테스트")
    void 게시글로부터_답변들_가져오기_테스트() {
        Post post = postRepository.findById(1L).get(); // SELECT * FROM POST WHERE id = 1L;
        List<Comment> commentList = post.getCommentList(); // SELECT * FROM COMMENT WHERE post_id = 1L; → DB 끊김 (LAZY 로딩 때문에) → @Transactional

        assertThat(commentList.size()).isEqualTo(2);
        assertThat(commentList.get(0).getContent()).isEqualTo("1번 답변입니다.");
    }
}
