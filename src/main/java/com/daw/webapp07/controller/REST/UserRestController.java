package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.ProjectDetailsDTO;
import com.daw.webapp07.DTO.ProjectPreviewDTO;
import com.daw.webapp07.DTO.UserDetailsDTO;
import com.daw.webapp07.DTO.UserPreviewDTO;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.RepositoryUserDetailsService;
import com.daw.webapp07.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserRestController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @GetMapping("/users")
    public ResponseEntity<Iterable<UserPreviewDTO>> getUsers() {
        List<UserEntity> usersdb =  userService.findAll();
        Collection<UserPreviewDTO> users = new ArrayList<>();
        for (UserEntity userdb : usersdb) {
            users.add(new UserPreviewDTO(userdb));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}/")
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
}
