package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.CommentDTO;
import com.daw.webapp07.DTO.ImageDTO;
import com.daw.webapp07.DTO.ProjectDetailsDTO;
import com.daw.webapp07.DTO.ProjectPreviewDTO;
import com.daw.webapp07.model.Comment;
import com.daw.webapp07.model.Image;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.CommentService;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api")
public class ProjectRestController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


    @GetMapping("/projects")
    public ResponseEntity<Iterable<ProjectPreviewDTO>> getProjects(Pageable page, HttpServletRequest request) {
        int size = 10;
        int pageNumber = page.getPageNumber();
        Page<Project> projects;
        Collection<ProjectPreviewDTO> projectPreviewDTO = new ArrayList<>();

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            Optional<UserEntity> user = userService.findUserByName(principal.getName());
            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            projects = projectService.searchRecommendedProjects(pageNumber, size, user.get().getId());

        } else {
            projects = projectService.searchProjects(pageNumber, size);
        }

        for (Project project : projects) {
            projectPreviewDTO.add(new ProjectPreviewDTO(project));
        }

        return new ResponseEntity<>(projectPreviewDTO, HttpStatus.OK);
    }

    @GetMapping("/projects/{id}/")
    public ResponseEntity<ProjectDetailsDTO> getProject(@PathVariable long id) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO(project);
            return new ResponseEntity<>(projectDetailsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*YA ESTABA HECHO
    @GetMapping("/projects/{id}/comments")
    public ResponseEntity<Iterable<CommentDTO>> getComments(@PathVariable long id, Pageable page, int size) {
        int pageNumber = page.getPageNumber();
        int sizePage = 10;
        Page<Comment> comments = commentService.searchCommentsProject(pageNumber, sizePage, id);
        return new ResponseEntity<>(commentService.toDTO(comments), HttpStatus.OK);
    }
     */

    @GetMapping("/projects/{id}/images/{idImage}")
    public ResponseEntity<Object> displayImage(@PathVariable Long id, @PathVariable Long idImage) throws SQLException {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if(checkProject == null){
            return ResponseEntity.notFound().build();
        }
        Project project = checkProject.get();
        Image image = projectService.getImage(project, idImage);
        if (image != null) {
            Resource file = new InputStreamResource(image.getImageFile().getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(image.getImageFile().length())
                    .body(file);
        }
        return ResponseEntity.notFound().build();

    }



    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectDetailsDTO> deleteProject(@PathVariable long id, HttpServletRequest request) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
                if (!project.getInversions().isEmpty() && request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())) {
                    projectService.deleteProject(id);
                    return new ResponseEntity<>(new ProjectDetailsDTO(project), HttpStatus.OK);
                }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/projects")
    public ResponseEntity<ProjectDetailsDTO> createProject(@RequestBody ProjectDetailsDTO project,
                              HttpServletRequest request) {

        if(project == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Project newProject = new Project();

        if(project.getCurrentAmount()!=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getDate()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getOwner()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (checkQuery.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserEntity query = checkQuery.get();
        newProject.setOwner(query);
        LocalDate date = LocalDate.now();
        newProject.setDate(date);

        projectService.saveProject(newProject);

        return new ResponseEntity<>(new ProjectDetailsDTO(newProject), HttpStatus.CREATED);
    }

    @PostMapping("/projects/{projectId}/images")
    public ResponseEntity<Object> uploadImage(@PathVariable long id,
                                              @RequestParam MultipartFile[] files){
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project project = checkProject.get();

        for (MultipartFile file : files) {
            Image image = new Image(file);
            project.addImage(image);
        }
            projectService.saveProject(project);
            return ResponseEntity.ok().build();


        }




    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> replaceProject(@PathVariable long id,
                                                @RequestBody Project newProject) {
        Optional<Project> project = projectService.getOptionalProject(id);
        if (project.isPresent()) {
            if(newProject.getCurrentAmount()!=project.get().getCurrentAmount()) return ResponseEntity.badRequest().build();
            if(!newProject.getInversions().equals(project.get().getInversions())) return ResponseEntity.badRequest().build();
            if(!newProject.getDate().equals(project.get().getDate())) return ResponseEntity.badRequest().build();
            newProject.setId(id);
            projectService.saveProject(newProject);
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }








}
