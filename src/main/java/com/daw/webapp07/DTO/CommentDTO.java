package com.daw.webapp07.DTO;

import com.daw.webapp07.model.Comment;


import java.sql.SQLException;
import java.util.List;

public class CommentDTO {
    private Long id;

    private UserPreviewDTO user;
    private String text;


    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        Long id1 = comment.getUser().getProfilePhoto().getId();
        this.text = comment.getText();
        this.user = new UserPreviewDTO(comment.getUser());

    }

    /* METER EN USERRESTCONTROLLER
    @GetMapping("/users/{id}/profile")
    public ResponseEntity<Object> displayProfilePhoto(@PathVariable Long id) throws SQLException {
        UserEntity userEntity = userService.findUserById(id).orElseThrow();
        Resource file = new InputStreamResource(userEntity.getProfilePhoto().getImageFile().getBinaryStream());

        return ResponseEntity.ok()
                .contentLength(userEntity.getProfilePhoto().getImageFile().length())
                .body(file);

    }

     */

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

}
