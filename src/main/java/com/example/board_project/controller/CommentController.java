package com.example.board_project.controller;

import com.example.board_project.dto.CommentRequest;
import com.example.board_project.entity.Comment;
import com.example.board_project.entity.Post;
import com.example.board_project.entity.SiteUser;
import com.example.board_project.service.CommentService;
import com.example.board_project.service.PostService;
import com.example.board_project.service.UserService;
import com.example.board_project.util.DataNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    // 댓글 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/write/{id}")
    public String addComment(Principal principal, Model model, @PathVariable long id, @Valid CommentRequest commentRequest, BindingResult bindingResult) {
        Post post = postService.getPost(id);
        SiteUser siteUser = userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post_detail";
        }

        Comment comment = commentService.addComment(post, commentRequest.getContent(), siteUser);

        return "redirect:/post/detail/%d#comment_%s".formatted(comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/modify/{id}")
    public String modifyComment(CommentRequest commentRequest, @PathVariable Long id, Principal principal) {
        Comment comment = commentService.getComment(id);

        if (comment == null) {
            throw new DataNotFoundException("댓글이 존재하지 않습니다.");
        }

        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentRequest.setContent(comment.getContent());

        return "comment_form";
    }

    // 댓글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/modify/{id}")
    public String modifyComment(@Valid CommentRequest commentRequest, @PathVariable Long id, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment comment = commentService.getComment(id);

        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentService.modify(comment, commentRequest.getContent());

        return "redirect:/post/detail/%d#comment_%s".formatted(comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id, Principal principal) {
        Comment comment = commentService.getComment(id);

        if (comment == null) {
            throw new DataNotFoundException("댓글이 존재하지 않습니다.");
        }

        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        commentService.delete(comment);

        return "redirect:/post/detail/%d#comment_%s".formatted(comment.getPost().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("comment/vote/{id}")
    public String answerVote(Principal principal, @PathVariable Long id) {
        Comment comment = commentService.getComment(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        commentService.vote(comment, siteUser);

        return "redirect:/post/detail/%d#comment_%s".formatted(comment.getPost().getId(), comment.getId());
    }
}
