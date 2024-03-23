package com.daw.webapp07.DTO;

import com.daw.webapp07.model.Comment;


import java.sql.SQLException;
import java.util.List;

public class CommentDTO {
    private Long id;
    private UserPreviewDTO user;
    private String text;
    private Long projectId;


    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.user = new UserPreviewDTO(comment.getUser());
        this.projectId = comment.getProject().getId();

    }


    public static Iterable<CommentDTO> listCommentDTO(List<Comment> comments) {
        return comments.stream().map(CommentDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserPreviewDTO getUser() {
        return user;
    }

    public void setUser(UserPreviewDTO user) {
        this.user = user;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

}
