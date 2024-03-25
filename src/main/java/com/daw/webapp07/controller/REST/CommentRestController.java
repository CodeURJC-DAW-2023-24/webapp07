package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.CommentDTO;
import com.daw.webapp07.model.Comment;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.CommentService;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Get all comments", description = "Returns all comments of all projects.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to update images for the project."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })

    @GetMapping("/comments")
    public ResponseEntity<Iterable<CommentDTO>> getComments(Pageable page) {
        int pageNumber = page.getPageNumber();
        int pageSize = 10;

        Page<Comment> comments = commentService.searchComments(pageNumber, pageSize);
        return new ResponseEntity<>(commentService.toDTO(comments), HttpStatus.OK);

    }

    @Operation(summary = "Get project comments", description = "Returns comments of a specific project by project ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })

    @GetMapping("/comments/projects/{projectId}/")
    public ResponseEntity<Iterable<CommentDTO>> getProjectComments(@PathVariable long projectId, Pageable page) {
        int pageNumber = page.getPageNumber();
        int pageSize = 10;

        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject.isPresent() ) {
            Page<Comment> comments = commentService.searchCommentsProject(pageNumber, pageSize, projectId);
            return new ResponseEntity<>(commentService.toDTO(comments), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get comment by ID", description = "Returns a comment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Comment not found. The comment with the specified ID could not be found.")
    })

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

    @Operation(summary = "Delete comment by ID", description = "Deletes a comment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to delete the comment."),
            @ApiResponse(responseCode = "404", description = "Comment not found. The comment with the specified ID could not be found.")
    })

    @DeleteMapping("/comments/{id}/")
    public ResponseEntity<CommentDTO> deleteComment(@PathVariable long id, HttpServletRequest request) {
        Optional<Comment> checkComment = commentService.getComment(id);
        if (checkComment.isPresent()) {
            Comment comment = checkComment.get();
            long projectId = comment.getProject().getId();
            Optional<Project> checkProject = projectService.getOptionalProject(projectId);
            if (!checkProject.isEmpty()){
                Project project = checkProject.get();
                if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())) {
                    project.deleteComment(comment);
                    commentService.deleteComment(id);
                    projectService.saveProject(project);
                    return new ResponseEntity<>(new CommentDTO(comment), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete project comment by ID", description = "Deletes a comment of a specific project by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to delete the comment."),
            @ApiResponse(responseCode = "404", description = "Comment not found. The comment with the specified ID could not be found.")
    })

    @DeleteMapping("/comments/{projectId}/{id}/")
    public ResponseEntity<CommentDTO> deleteProjectComment(@PathVariable long id, @PathVariable long projectId, HttpServletRequest request) {
        Optional<Comment> checkComment = commentService.getComment(id);
        if (checkComment.isPresent()) {
            Comment comment = checkComment.get();
            Optional<Project> checkProject = projectService.getOptionalProject(projectId);
            if (!checkProject.isEmpty()){
                Project project = checkProject.get();
                if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())) {
                    project.deleteComment(comment);
                    commentService.deleteComment(id);
                    projectService.saveProject(project);
                    return new ResponseEntity<>(new CommentDTO(comment), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Operation(summary = "Create project comment", description = "Creates a new comment for a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })

    @PostMapping("/comments/{projectId}/")
    public ResponseEntity<CommentDTO> createComment(@RequestBody String text, @PathVariable long projectId, HttpServletRequest request){
        Comment newComment = new Comment();
        newComment.setText(text);

        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            newComment.setProject(project);

            Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
            if (checkQuery == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            UserEntity query = checkQuery.get();
            newComment.setUser(query);
            newComment.setUserName(query.getName());
            project.addComment(newComment);
            projectService.saveProject(project);

            return new ResponseEntity<>(new CommentDTO(newComment), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
