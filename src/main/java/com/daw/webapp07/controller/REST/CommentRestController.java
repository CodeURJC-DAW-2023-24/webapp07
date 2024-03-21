package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.CommentDTO;
import com.daw.webapp07.model.Comment;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.CommentService;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping("/comments")
    public ResponseEntity<Iterable<CommentDTO>> getComments() {
        return new ResponseEntity<>(commentService.getComments(), HttpStatus.OK);
    }

    @GetMapping("/comments/{projectId}/comments/")
    public ResponseEntity<Iterable<CommentDTO>> getProjectComments(@PathVariable long projectId) {
        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject.isPresent() ) {
            Project project = checkProject.get();
            return new ResponseEntity<>(commentService.toDTO(project.getComments()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comments/{id}/")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long id) {
        Optional<Comment> checkComment = commentService.getComment(id);
        if (checkComment.isPresent()) {
            Comment comment = checkComment.get();
            return new ResponseEntity<>(new CommentDTO(comment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comments/{projectId}/{id}/")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long projectId, @PathVariable long id) {
        Optional<Comment> checkComment = commentService.getComment(id);
        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkComment.isPresent() && checkProject.isPresent() ) {
            Comment comment = checkComment.get();
            return new ResponseEntity<>(new CommentDTO(comment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/comments/{projectId}/")
    public ResponseEntity<CommentDTO> createComment(@RequestBody Comment comment, @PathVariable long projectId, HttpServletRequest request){
        Comment newComment = new Comment();
        newComment.setText(comment.getText());

        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            newComment.setProject(project);

            Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
            if (checkQuery == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            UserEntity query = checkQuery.get();
            newComment.setUser(query);
            project.addComment(newComment);
            projectService.saveProject(project);

            return new ResponseEntity<>(new CommentDTO(newComment), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
