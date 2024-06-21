package com.example.board_project.repository;

import com.example.board_project.entity.Post;
import com.example.board_project.entity.SiteUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private static Long lastSampleDataId;

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    private void clearData() {
        clearData(postRepository, userRepository);
    }

    public static void clearData(PostRepository postRepository, UserRepository userRepository) {
        postRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.truncateTable();
    }

    public static Long createSampleData(PostRepository postRepository, UserRepository userRepository) {
        SiteUser author = new SiteUser();
        author.setUsername("author"); // 유저 이름 설정
        userRepository.save(author);

        Post post1 = new Post();
        post1.setTitle("1번 제목입니다.");
        post1.setContent("1번 내용입니다.");
        post1.setCreateDate(LocalDateTime.now());
        post1.setAuthor(author);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("2번 제목입니다.");
        post2.setContent("2번 내용입니다.");
        post2.setCreateDate(LocalDateTime.now());
        post2.setAuthor(author);
        postRepository.save(post2);

        return post2.getId();
    }

    private void createSampleData() {
        lastSampleDataId = createSampleData(postRepository, userRepository);
    }

    @Test
    void createManySamepleData() {
        boolean run = false;

        if (!run) return;

        IntStream.rangeClosed(3, 300).forEach(id -> {
            Post post = new Post();
            post.setTitle("%d번 게시글입니다.".formatted(id));
            post.setContent("%d번 게시글의 내용입니다.".formatted(id));
            post.setCreateDate(LocalDateTime.now());
            postRepository.save(post);
        });
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void 게시글_등록_테스트() {
        SiteUser author = userRepository.findByUsername("author").orElseThrow();

        Post post1 = new Post();
        post1.setTitle("3번 제목입니다.");
        post1.setContent("3번 내용입니다.");
        post1.setCreateDate(LocalDateTime.now());
        post1.setAuthor(author);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("4번 제목입니다.");
        post2.setContent("4번 내용입니다.");
        post2.setCreateDate(LocalDateTime.now());
        post2.setAuthor(author);
        postRepository.save(post2);

        assertThat(post1.getId()).isEqualTo(lastSampleDataId + 1);
        assertThat(post2.getId()).isEqualTo(lastSampleDataId + 2);
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void 게시글_목록_조회_테스트() {
        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(lastSampleDataId.intValue());

        Post post = all.get(0);
        assertEquals("1번 제목입니다.", post.getTitle());
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void 게시글_조회_테스트() {
        Optional<Post> optionalPost = postRepository.findById(1L);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            assertEquals("1번 제목입니다.", post.getTitle());
        }
    }

    @Test
    @DisplayName("게시글 제목 조회 테스트")
    void 게시글_제목_조회_테스트() {
        Post post = postRepository.findByTitle("1번 제목입니다.");
        assertEquals(1, post.getId());
    }

    @Test
    @DisplayName("게시글 제목과 내용 일치하는 게시물 조회 테스트")
    void 게시글_제목_내용_조회_테스트() {
        Post q = postRepository.findByTitleAndContent(
                "1번 제목입니다.", "1번 내용입니다.");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("게시글 특정 단어로 시작하는 제목 조회 테스트")
    void 게시글_특정_단어로_시작하는_제목_조회_테스트() {
        List<Post> postList = postRepository.findByTitleLike("1번%");
        Post post = postList.get(0);
        assertEquals("1번 제목입니다.", post.getTitle());
    }

    @Test
    @DisplayName("게시글 특정 단어로 시작하는 내용 조회 테스트")
    void 게시글_특정_단어로_시작하는_내용_조회_테스트() {
        List<Post> postList = postRepository.findByContentLike("1번%");
        Post post = postList.get(0);
        assertEquals("1번 내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void 게시글_수정_테스트() {
        assertThat(postRepository.count()).isEqualTo(lastSampleDataId);

        Post post = postRepository.findById(1L).get();
        post.setTitle("수정된 제목");
        postRepository.save(post);

        post = postRepository.findById(1L).get();
        assertThat(post.getTitle()).isEqualTo("수정된 제목");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void 게시글_삭제_테스트() {
        assertThat(postRepository.count()).isEqualTo(lastSampleDataId);

        Post post = postRepository.findById(1L).get();
        postRepository.delete(post);
        assertThat(postRepository.count()).isEqualTo(lastSampleDataId - 1);
    }
}
