package com.example.board_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Operation(summary = "메인 페이지", description = "게시판 리스트 페이지로 이동")
    @GetMapping("/")
    public String root() {
        return "redirect:/post/list";
    }
}
