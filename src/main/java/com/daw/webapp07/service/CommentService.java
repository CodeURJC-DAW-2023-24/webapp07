package com.daw.webapp07.service;

import com.daw.webapp07.model.Comment;
import com.daw.webapp07.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow();}

}
