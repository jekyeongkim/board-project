package com.example.board_project.repository;

import com.example.board_project.entity.Post;
import com.example.board_project.util.RepositoryUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, RepositoryUtil {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE POST ALTER COLUMN ID RESTART WITH 1", nativeQuery = true)
    void truncate();

    Post findByTitle(String title);

    Post findByTitleAndContent(String title, String content);

    List<Post> findByTitleLike(String title);

    List<Post> findByContentLike(String content);

    // 제목만 검색
    Page<Post> findByTitleContains(String keyword, Pageable pageable);
    // 제목 또는 내용 검색
    Page<Post> findByTitleContainsOrContentContains(String keyword1, String keyword2, Pageable pageable);

}
