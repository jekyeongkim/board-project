package com.example.board_project.util;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RepositoryUtil {
    @Transactional
    @Modifying
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE", nativeQuery = true)
    void disableForeignKeyChecks(); // 참조 무결성(외래 키 체크) 비활성화

    @Transactional
    @Modifying
    @Query(value = "SET REFERENTIAL_INTEGRITY TRUE", nativeQuery = true)
    void enableForeignKeyChecks(); // 참조 무결성(외래 키 체크) 활성화

    default void truncateTable() {
        // disableForeignKeyChecks();
        truncate();
        // enableForeignKeyChecks();
    }

    void truncate();
}
