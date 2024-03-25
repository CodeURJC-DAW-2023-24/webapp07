package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.*;
import com.daw.webapp07.model.*;
import com.daw.webapp07.service.RepositoryUserDetailsService;
import com.daw.webapp07.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RepositoryUserDetailsService repositoryUserDetailsService;

    @Operation(summary = "Get all users", description = "Returns a list of all users available in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserPreviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Users not found", content = @Content)
    })

    @GetMapping("/users")
    public ResponseEntity<Iterable<UserPreviewDTO>> getUsers() {
        List<UserEntity> usersdb =  userService.findAll();
        Collection<UserPreviewDTO> users = new ArrayList<>();
        for (UserEntity userdb : usersdb) {
            users.add(new UserPreviewDTO(userdb));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get user", description = "Returns a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailsDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found. The user with the specified ID could not be found.", content = @Content)
    })

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

    @Operation(summary = "Get user's inversions", description = "Returns a user's inversions by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inversions returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InversionDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
            @ApiResponse(responseCode = "404", description = "User not found. The user with the specified ID could not be found.")
    })

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

    @Operation(summary = "Get user's comments", description = "Returns a user's comments by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
            @ApiResponse(responseCode = "404", description = "User not found. The user with the specified ID could not be found.")
    })

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

    @Operation(summary = "Get user's projects", description = "Returns a user's projects by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectPreviewDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
            @ApiResponse(responseCode = "404", description = "User not found. The user with the specified ID could not be found.")
    })

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

    @Operation(summary = "Get user's profile photo", description = "Returns user's photo by his id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo returned successfully"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })

    @GetMapping("/users/{id}/profile")
    public ResponseEntity<Object> displayProfilePhoto(@PathVariable Long id) throws SQLException{
        UserEntity userEntity = userService.findUserById(id).orElseThrow();
        Resource file = new InputStreamResource(userEntity.getProfilePhoto().getImageFile().getBinaryStream());

        return ResponseEntity.ok()
                .contentLength(userEntity.getProfilePhoto().getImageFile().length())
                .body(file);

    }


    @Operation(summary = "Creates a new user", description = "Creates a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailsDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized."),
            @ApiResponse(responseCode = "404", description = "Not found. The specified resource could not be found.")
    })

    @PostMapping("/users")
    public ResponseEntity<UserDetailsDTO> createUser(@RequestBody UserEntity user){

        user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
        user.setRoles(List.of("USER"));
        if(repositoryUserDetailsService.registerUser(user)){
            URI location = fromCurrentRequest().path("/{id}/").buildAndExpand(user.getId()).toUri();
            return ResponseEntity.created(location).body(new UserDetailsDTO(user));
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Edits user's profile", description = "Edits the profile of an existing user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized."),
            @ApiResponse(responseCode = "404", description = "Not found. The specified user could not be found.")
    })

    @PutMapping("/users")
    public ResponseEntity<UserEntity> editUser(@RequestBody UserEntity newUser, HttpServletRequest request) {

        String name = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userService.findUserByName(name);
        if (user.isPresent()) {
            user.get().setEmail(newUser.getEmail());
            userService.saveUser(user.get());
            return ResponseEntity.ok().build();
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Edits user's profile photo", description = "Edits the profile photo of an existing user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile photo updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized."),
            @ApiResponse(responseCode = "404", description = "Not found. The specified user could not be found.")
    })

    @PutMapping("/users/images")
    public ResponseEntity<UserEntity> editProfilePicture(@RequestParam MultipartFile file, HttpServletRequest request){

        Optional<UserEntity> checkUser = userService.findUserByName(request.getUserPrincipal().getName());
        if(checkUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        UserEntity user = checkUser.get();

        if(request.getUserPrincipal().getName().equals(user.getName())){
            Image image = new Image(file);
            user.setProfilePhoto(image);

            userService.saveUser(user);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }



}
