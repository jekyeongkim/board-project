package com.example.board_project.repository;

import com.example.board_project.entity.Comment;
import com.example.board_project.util.RepositoryUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long>, RepositoryUtil {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE COMMENT ALTER COLUMN ID RESTART WITH 1", nativeQuery = true)
    void truncate();
}
