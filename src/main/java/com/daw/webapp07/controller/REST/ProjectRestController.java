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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Iterable<ProjectPreviewDTO>> getProjects( @RequestParam(name = "page", defaultValue = "0") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Project> projects = projectService.searchProjects(page, size);
        Collection<ProjectPreviewDTO> projectPreviewDTO = new ArrayList<>();
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

    @GetMapping("/projects/{id}/images")
    public ResponseEntity<Iterable<ImageDTO>> getImages(@PathVariable long id, @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            List<ImageDTO> returnList = new ArrayList<>();
            int start = page*size;
            int end = Math.min(project.getImages().size(), start+size);
            if(start<project.getImages().size()){
                try {
                    for(int i=start; i<end; i++){
                        returnList.add(new ImageDTO(project.getImages().get(i),i));
                    }
                }
                catch (Exception e){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project/{id}/comments")
    public ResponseEntity<Iterable<CommentDTO>> getComments(@PathVariable long id, @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Comment> comments = commentService.searchCommentsProject(page, size, id);
        return new ResponseEntity<>(commentService.toDTO(comments), HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                              @RequestParam("file") MultipartFile[] files,
                              HttpServletRequest request) {
        if(project == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getInversions()==null){
            project.setInversions(new ArrayList<>());
        }
        else{
            if(!project.getInversions().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(project.getComments()==null){
            project.setComments(new ArrayList<>());
        }
        else{
            if(!project.getComments().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(project.getImages()==null){
            project.setImages(new ArrayList<>());
        }
        if(project.getCurrentAmount()!=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getDate()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getOwner()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (checkQuery.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserEntity query = checkQuery.get();
        project.setOwner(query);
        LocalDate date = LocalDate.now();
        project.setDate(date);

        for (MultipartFile file : files) {
            Image image = new Image(file);
            project.addImage(image);
        }
        projectService.saveProject(project);

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable long id) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            projectService.deleteProject(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/project/{id}")
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
