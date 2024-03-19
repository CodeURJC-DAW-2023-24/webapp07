package com.daw.webapp07.controller;

import com.daw.webapp07.model.Image;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProjectRestController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;
    @GetMapping("/project-details/{id}/")
    public ResponseEntity<Project> getProject(@PathVariable long id) {

        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                              @RequestParam("file") MultipartFile[] files,
                              HttpServletRequest request) {

        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
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
            if(newProject.getCurrentAmount()!=project.get().getCurrentAmount()) {
                return ResponseEntity.badRequest().build();
            }
            if(!newProject.getInversions().equals(project.get().getInversions())){
                    return ResponseEntity.badRequest().build();
            }
            if(!newProject.getDate().equals(project.get().getDate())) {
                return ResponseEntity.badRequest().build();
            }
            newProject.setId(id);
            projectService.saveProject(newProject);
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}
