package com.example.board_project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    @NotEmpty(message = "제목을 입력하세요")
    @Size(max = 200, message = "제목은 200자 이하로 작성해주세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;

}
