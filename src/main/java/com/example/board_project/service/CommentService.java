package com.example.board_project.service;

import com.example.board_project.entity.Comment;
import com.example.board_project.entity.Post;
import com.example.board_project.entity.SiteUser;
import com.example.board_project.repository.CommentRepository;
import com.example.board_project.util.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Post post, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setAuthor(author);

        post.addComment(comment);

        commentRepository.save(comment);

        return comment;
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("commen not found"));
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public void vote(Comment comment, SiteUser siteUser) {
        comment.getVoter().add(siteUser);
        commentRepository.save(comment);
    }
}
