package com.example.board_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE, CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    @Column(name = "view_count", nullable = false, columnDefinition = "int default 0")
    private int viewCount = 0;
    public void addComment(Comment comment) {
        comment.setPost(this);
        getCommentList().add(comment);
    }
}
