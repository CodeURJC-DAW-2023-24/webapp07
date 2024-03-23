package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.*;
import com.daw.webapp07.model.Comment;
import com.daw.webapp07.model.Inversion;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.RepositoryUserDetailsService;
import com.daw.webapp07.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public ResponseEntity<Iterable<UserPreviewDTO>> getUsers() {
        List<UserEntity> usersdb =  userService.findAll();
        Collection<UserPreviewDTO> users = new ArrayList<>();
        for (UserEntity userdb : usersdb) {
            users.add(new UserPreviewDTO(userdb));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable long id) {
        Optional<UserEntity> userdb = userService.findUserById(id);
        if (userdb.isPresent()) {
            UserEntity user = userdb.get();
            UserDetailsDTO userDTO = new UserDetailsDTO(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}/inversions")
    public ResponseEntity<Iterable<InversionDTO>> getUserInversions(@PathVariable long id) {
        Optional<UserEntity> userdb = userService.findUserById(id);
        if (userdb.isPresent()) {
            List<Inversion> inversions = userdb.get().getInversions();
            Collection<InversionDTO> inversionsDTO = new ArrayList<>();
            for (Inversion inversion : inversions) {
                inversionsDTO.add(new InversionDTO(inversion));
            }
            return new ResponseEntity<>(inversionsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}/comments")
    public ResponseEntity<Iterable<CommentDTO>> getUserComments(@PathVariable long id) {
        Optional<UserEntity> userdb = userService.findUserById(id);
        if (userdb.isPresent()) {
            List<Comment> comments = userdb.get().getComments();
            Collection<CommentDTO> commentsDTO = new ArrayList<>();
            for (Comment comment : comments) {
                commentsDTO.add(new CommentDTO(comment));
            }
            return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}/projects")
    public ResponseEntity<Iterable<ProjectPreviewDTO>> getUserProjects(@PathVariable long id) {
        Optional<UserEntity> userdb = userService.findUserById(id);
        if (userdb.isPresent()) {
            List<Project> projects = userdb.get().getProjects();
            System.out.println("AAAAAAAAAAAAA" + projects.size());
            Collection<ProjectPreviewDTO> projectsDTO = new ArrayList<>();
            for (Project project : projects) {
                projectsDTO.add(new ProjectPreviewDTO(project));
            }
            return new ResponseEntity<>(projectsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/users/{id}/profile")
    public ResponseEntity<Object> displayProfilePhoto(@PathVariable Long id) throws SQLException{
        UserEntity userEntity = userService.findUserById(id).orElseThrow();
        Resource file = new InputStreamResource(userEntity.getProfilePhoto().getImageFile().getBinaryStream());

        return ResponseEntity.ok()
                .contentLength(userEntity.getProfilePhoto().getImageFile().length())
                .body(file);

    }


}
