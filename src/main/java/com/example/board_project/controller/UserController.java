package com.example.board_project.controller;

import com.example.board_project.dto.UserAccountRequest;
import com.example.board_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입 페이지", description = "회원가입 페이지로 이동")
    @GetMapping("/join")
    public String signup(UserAccountRequest userAccountRequest) {
            return "join_form";
    }

    @Operation(summary = "회원가입 페이지", description = "유효성 검사에 따라 회원가입 성공 또는 실패")
    @PostMapping("/join")
    public String signup(@Valid UserAccountRequest userAccountRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "join_form";
        }

        if (!userAccountRequest.getPassword1().equals(userAccountRequest.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "패스워드가 일치하지 않습니다.");
            return "join_form";
        }

        try {
            userService.create(userAccountRequest.getUsername(), userAccountRequest.getPassword1(), userAccountRequest.getEmail());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "join_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "join_form";
        }

        return "redirect:/post/list";
    }
    @Operation(summary = "로그인 페이지", description = "로그인 페이지로 이동")
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
