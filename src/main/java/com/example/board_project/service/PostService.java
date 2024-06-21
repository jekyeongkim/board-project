package com.example.board_project.service;

import com.example.board_project.entity.Post;
import com.example.board_project.entity.SiteUser;
import com.example.board_project.repository.PostRepository;
import com.example.board_project.util.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void addPost(String title, String content, SiteUser author) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCreateDate(LocalDateTime.now());
        post.setAuthor(author);
        postRepository.save(post);
    }

    public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        postRepository.save(post);
    }

    public Page<Post> getList(String keyword, Pageable pageable) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        sorts.add(Sort.Order.desc("id")); // 작성날짜가 같은 경우 id 기준으로 정렬

        Sort sort = Sort.by(sorts);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (keyword == null || keyword.trim().length() == 0) {
            return postRepository.findAll(sortedPageable);
        }

        return postRepository.findByTitleContains(keyword, sortedPageable);
    }

    public Post getPost(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("post is not found"));
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public void vote(Post post, SiteUser siteUser) {
        post.getVoter().add(siteUser);
        postRepository.save(post);
    }

    public void unvote(Post post, SiteUser siteUser) {
        post.getVoter().remove(siteUser);
        postRepository.save(post);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("post is not found"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }
}
