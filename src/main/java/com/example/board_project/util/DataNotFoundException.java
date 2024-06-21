package com.example.board_project.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "게시글이 존재하지 않습니다.")
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String board_not_found) {
    }
}
