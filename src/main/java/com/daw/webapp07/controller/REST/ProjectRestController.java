package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.*;
import com.daw.webapp07.model.*;
import com.daw.webapp07.service.GraphicsService;
import com.daw.webapp07.service.InversionService;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

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
    private GraphicsService graphicsService;
    @Autowired
    private InversionService inversionService;



    @Operation(summary = "Get all projects", description = "Returns a list of all projects available in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of projects returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectPreviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Projects not found", content = @Content)
    })

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

    @Operation(summary = "Get a project by ID", description = "Returns a project by searching his ID in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of projects returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDetailsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })

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


    @Operation(summary = "Get a project's image by ID", description = "Returns a project's image by searching his ID in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of projects returned successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Projects not found", content = @Content)
    })

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

    @Operation(summary = "Get all project's inversions", description = "Returns a project's inversions by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inversions returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InversionDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
            @ApiResponse(responseCode = "404", description = "Project not found. The user with the specified ID could not be found.")
    })

    @GetMapping("/projects/{id}/inversions")
    public ResponseEntity<Iterable<InversionDTO>> getInversions(@PathVariable long id) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            Collection<InversionDTO> inversionDTO = new ArrayList<>();
            for (int i = 0; i < project.getInversions().size(); i++) {
                inversionDTO.add(new InversionDTO(project.getInversions().get(i)));
            }
            return new ResponseEntity<>(inversionDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get project's inversion by ID", description = "Returns a project's specific inversion by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inversions returned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InversionDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
            @ApiResponse(responseCode = "404", description = "Project not found. The user with the specified ID could not be found.")
    })

    @GetMapping("/projects/{id}/inversions/{inversionId}")
    public  ResponseEntity<InversionDTO> getInversion(@PathVariable long inversionId){
        Optional<Inversion> checkInversion = inversionService.getInversion(inversionId);
        if (checkInversion.isPresent()) {
            Inversion inversion = checkInversion.get();
            InversionDTO inversionDTO = new InversionDTO(inversion);
            return new ResponseEntity<>(inversionDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "Delete a project by ID", description = "Deletes a project by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDetailsDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied. The provided project ID is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to delete the project."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectDetailsDTO> deleteProject(@PathVariable long id, HttpServletRequest request) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
                if (project.getInversions().isEmpty() && (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName()))) {
                    projectService.deleteProject(id);
                    return new ResponseEntity<>(new ProjectDetailsDTO(project), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new project", description = "Creates a new project based on the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDetailsDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to create the project."),
            @ApiResponse(responseCode = "404", description = "User not found. The user making the request could not be found.")
    })

    @PostMapping("/projects")
    public ResponseEntity<Object> createProject(@RequestBody Project project,
                                                           HttpServletRequest request) {
        if(project == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getCurrentAmount()!=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getDate()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getOwner()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (!checkQuery.isEmpty()){
            UserEntity query = checkQuery.get();
            project.setOwner(query);
            LocalDate date = LocalDate.now();
            project.setDate(date);

            ArrayList<Project> userProjects = new ArrayList<>(query.getProjects());
            userProjects.add(project);
            query.setProjects(userProjects);

            userService.saveUser(query);
            projectService.saveProject(project);

            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(project.getId()).toUri();
            return ResponseEntity.created(location).body(project);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Create a new inversion for a project", description = "Creates a new inversion for the specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inversion created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inversion.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to create the inversion."),
            @ApiResponse(responseCode = "404", description = "Project or user not found. The project or user making the request could not be found.")
    })

    @PostMapping("/projects/{projectId}/inversions")
    public ResponseEntity<Object> createInversion(@PathVariable long projectId,
                                                  @RequestBody Inversion inversion,
                                                  HttpServletRequest request) {
        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if(checkProject.isEmpty()) return ResponseEntity.notFound().build();
        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if(checkQuery.isEmpty()) return ResponseEntity.notFound().build();
        if(inversion == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(inversion.getDate()==null) inversion.setDate(LocalDate.now());
        if(!inversion.getDate().isEqual(LocalDate.now())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(inversion.getAmount()<=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(inversion.getUser()==null) inversion.setUser(checkQuery.get());
        if(inversion.getUser()!=checkQuery.get()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(inversion.getProject()==null) inversion.setProject(checkProject.get());
        if(inversion.getProject()!=checkProject.get()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Project project = checkProject.get();
        project.addInversion(inversion);
        projectService.saveProject(project);
        checkQuery.get().addInversion(inversion);
        userService.saveUser(checkQuery.get());

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(inversion.getId()).toUri();
        return ResponseEntity.created(location).body(inversion);

    }

    @Operation(summary = "Upload images for a project", description = "Uploads images for the specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to upload images for the project."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })

    @PostMapping("/projects/{projectId}/images")
    public ResponseEntity<Object> uploadImages(@PathVariable long projectId,
                                               @RequestParam MultipartFile[] files, HttpServletRequest request){
        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project project = checkProject.get();

        if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())) {

            project.getImages().clear();

            for (MultipartFile file : files) {
                Image image = new Image(file);
                project.addImage(image);
            }
            projectService.saveProject(project);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Update images for a project", description = "Updates images for the specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to update images for the project."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })

    @PutMapping("/projects/{projectId}/images")
    public ResponseEntity<Object> updateImages(@PathVariable long projectId,
                                               @RequestParam MultipartFile[] files, HttpServletRequest request){
        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project project = checkProject.get();

        if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())) {

            for (MultipartFile file : files) {
                Image image = new Image(file);
                project.addImage(image);
            }
            projectService.saveProject(project);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }



    @Operation(summary = "Update a project", description = "Updates the details of the specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectDetailsDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. The request body is invalid."),
            @ApiResponse(responseCode = "403", description = "Forbidden. The request is not authorized to update the project."),
            @ApiResponse(responseCode = "404", description = "Project not found. The project with the specified ID could not be found.")
    })


    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectDetailsDTO> updateProject(@PathVariable Long id,
                                                           @RequestBody Project project,
                                                           HttpServletRequest request) {
        if(project == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getDate()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(project.getOwner()!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Project oldProj = checkProject.get();
        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (!checkQuery.isEmpty() && (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(oldProj.getOwner().getName()))) {
            if (project.getName() != null) oldProj.setName(project.getName());
            if (project.getDescription() != null) oldProj.setDescription(project.getDescription());
            if (project.getCategory() != null) oldProj.setCategory(project.getCategory());
            if (project.getUrl() != null) oldProj.setUrl(project.getUrl());
            if (project.getGoal() != 0 && project.getGoal() > 0) oldProj.setGoal(project.getGoal());

            projectService.saveProject(oldProj);
            return new ResponseEntity<>(new ProjectDetailsDTO(oldProj), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
