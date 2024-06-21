package com.example.board_project.repository;

import com.example.board_project.entity.SiteUser;
import com.example.board_project.util.RepositoryUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long>, RepositoryUtil {
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE SITE_USER ALTER COLUMN ID RESTART WITH 1", nativeQuery = true)
    void truncate();

    Optional<SiteUser> findByUsername(String username);
}
