package com.example.board_project.controller;

import com.example.board_project.dto.CommentRequest;
import com.example.board_project.dto.PostRequest;
import com.example.board_project.entity.Post;
import com.example.board_project.entity.SiteUser;
import com.example.board_project.service.PostService;
import com.example.board_project.service.UserService;
import com.example.board_project.util.DataNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // 글 작성 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    public String write(PostRequest postRequest) {
        return "write_form";
    }

    // 글 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/write")
    public String addPost(@Valid PostRequest postRequest, BindingResult bindingResult, Principal principal) {

        SiteUser siteUser = userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            return "write_form";
        }

        postService.addPost(postRequest.getTitle(), postRequest.getContent(), siteUser);
        return "redirect:/post/list";
    }

    // 게시글 수정 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/modify/{id}")
    public String modifyPost(PostRequest postRequest, @PathVariable Long id, Principal principal) {
        Post post = postService.getPost(id);

        if (post == null) {
            throw new DataNotFoundException("%d번 게시글이 존재하지 않습니다.".formatted(id));
        }

        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        postRequest.setTitle(post.getTitle());
        postRequest.setContent(post.getContent());
        return "write_form";
    }

    // 게시글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/modify/{id}")
    public String modifyPost(@Valid PostRequest postRequest, @PathVariable Long id, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "write_form";
        }

        Post post = postService.getPost(id);

        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        postService.modify(post, postRequest.getTitle(), postRequest.getContent());
        return "redirect:/post/detail/%d".formatted(id);
    }

    // 게시글 목록
    @GetMapping("/post/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "") String keyword,
                       @PageableDefault(size = 15) Pageable pageable) {
        Page<Post> postList = postService.getList(keyword, pageable);
        paging(model, pageable, postList);

        model.addAttribute("postList", postList);
        model.addAttribute("pageable", pageable);
        model.addAttribute("keyword", keyword);
        return "post_list";
    }

    // 게시글 상세
    @GetMapping("/post/detail/{id}")
    public String detail(Model model, @PathVariable Long id, CommentRequest commentRequest) {
        postService.incrementViewCount(id); // 조회수 증가
        Post post = postService.getPost(id);

        model.addAttribute("post", post);
        return "post_detail";
    }

    // 게시글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/delete/{id}")
    public String deletePost(Principal principal, @PathVariable Long id) {
        Post post = postService.getPost(id);

        if (post == null) {
            throw new DataNotFoundException("%d번 게시글이 존재하지 않습니다.".formatted(id));
        }

        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        postService.delete(post);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/vote/{id}")
    public String votePost(Principal principal, @PathVariable Long id) {
        Post post = postService.getPost(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        postService.vote(post, siteUser);
        return "redirect:/post/detail/%d".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/unvote/{id}")
    public String unvotePost(Principal principal, @PathVariable Long id) {
        Post post = postService.getPost(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        postService.unvote(post, siteUser);
        return "redirect:/post/detail/%d".formatted(id);
    }

    private void paging(Model model, Pageable pageable, Page<Post> list) {
        int page = pageable.getPageNumber() + 1; // 0-based index to 1-based index
        int rows = pageable.getPageSize();
        int pageCount = 5; // 노출 페이지 개수
        int totalCount = (int) list.getTotalElements();

        int startPage = ((page - 1) / pageCount) * pageCount + 1;
        int endPage = ((page - 1) / pageCount + 1) * pageCount;
        int lastPage = (totalCount - 1) / rows + 1;

        if (endPage > lastPage) {
            endPage = lastPage;
        }

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("firstPage", 1);
        model.addAttribute("lastPage", lastPage);
    }
}
