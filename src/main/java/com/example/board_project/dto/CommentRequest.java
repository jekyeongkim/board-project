package com.example.board_project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotEmpty(message = "내용을 입력하세요")
    private String content;
}
