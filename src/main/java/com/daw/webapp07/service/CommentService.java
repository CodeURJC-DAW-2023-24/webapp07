package com.daw.webapp07.service;

import com.daw.webapp07.DTO.CommentDTO;
import com.daw.webapp07.model.Comment;
import com.daw.webapp07.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);}

    public Iterable<CommentDTO> getComments() {
        List<Comment> comments = commentRepository.findAll();
        return CommentDTO.listCommentDTO(comments);


    }

    public Iterable<CommentDTO> toDTO(List<Comment> comments) {
        return CommentDTO.listCommentDTO(comments);
    }
}
