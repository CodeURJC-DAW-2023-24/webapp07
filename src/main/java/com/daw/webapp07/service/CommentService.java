package com.daw.webapp07.service;

import com.daw.webapp07.DTO.CommentDTO;
import com.daw.webapp07.model.Comment;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Iterable<CommentDTO> toDTO(Page<Comment> comments) {
        return CommentDTO.listCommentDTO(comments.getContent());
    }

    public void deleteComment(long id) {
        commentRepository.deleteById(id);
        
    }

    public Page<Comment> searchComments(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return  commentRepository.findAll(pageable);
    }

    public Page<Comment> searchCommentsProject(int pageNumber, int pageSize, long projectId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return commentRepository.findByProjectId(projectId, pageable);
    }
}
