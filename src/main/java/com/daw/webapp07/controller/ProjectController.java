package com.daw.webapp07.controller;

import com.daw.webapp07.model.*;
import com.daw.webapp07.repository.*;
import com.daw.webapp07.service.*;


import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class ProjectController {



    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    InversionRepository inversionRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    GraphicsService graphicsService;

    @Autowired
    CommentService commentService;

    //This method will load the main page, differentiating the logged users from the guests.
    //Apart from executing the recommendation algorithm
    @GetMapping("/")
    public String innerPage(Model model, HttpServletRequest request) {
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userService.findUserByName(request.getUserPrincipal().getName());
            if(user.isPresent() && user.get().hasInversions()){
                System.out.println("Recomendando");
                model.addAttribute("projects", projectService.searchRecommendedProjects(0,10,user.get().getId()));
            }else
                model.addAttribute("projects", projectService.searchProjects(0, 10));

        }else
        {
            model.addAttribute("projects", projectService.searchProjects(0, 10));
        }


        return "inner-page";
    }

    // Returns more projects to not logged-in user
    @GetMapping("/projects")
    public String getProjects(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size ) {

        model.addAttribute("projects", projectService.searchProjects(page, size));

        return "portfolio";
    }


    // Returns more recommended projects to de user
    @GetMapping("/rec-projects")
    public String getRecProjects(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size ,  HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            // If user is not logged it gets redirected to login page
            return "redirect:/login";
        } else {
            Optional<UserEntity> user = userService.findUserByName(principal.getName());
            if (user.isPresent()) {
                model.addAttribute("projects", projectService.searchRecommendedProjects(page,size,user.get().getId()));
            }
            return "portfolio";
        }
    }



    //Shows more details about the project, differentiating between logged users, guest and admin
    @GetMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id, HttpServletRequest request) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();

        if(request.isUserInRole("USER")){
            if  (request.getUserPrincipal().getName().equals(project.getOwner().getName()) || request.isUserInRole("ADMIN")){
                model.addAttribute("privileged",true);
            }
        }
        model.addAttribute("project", project);
        model.addAttribute("id", id);

        graphicsService.initializeWith(project);
        model.addAttribute("donors",graphicsService.getNames());
        model.addAttribute("quantities", graphicsService.getQuantities());
        model.addAttribute("times",graphicsService.getTimes());
        model.addAttribute("pastmoney", graphicsService.getPastmoney());
        model.addAttribute("hasInversions", !project.getInversions().isEmpty());

        return "project-details";
    }


    //Get method for retrieving images
    @GetMapping("/projects/{id}/images/{index}")
    public ResponseEntity<Object> displayImage(@PathVariable Long id, @PathVariable int index) throws SQLException{
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if(checkProject == null){
            return ResponseEntity.notFound().build();
        }
        Project project = checkProject.get();
        index--; //index - 1 porque mustache empieza a contar desde 1
        List<Image> images = project.getImages();
        if (index < images.size()){
            Resource file = new InputStreamResource(images.get(index).getImageFile().getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(images.get(index).getImageFile().length())
                    .body(file);
        }
        return ResponseEntity.notFound().build();

    }


    //Edit profile page
    @GetMapping("/users/{id}/profile")
    public ResponseEntity<Object> displayProfilePhoto(@PathVariable Long id) throws SQLException{
        UserEntity userEntity = userService.findUserById(id).orElseThrow();
        Resource file = new InputStreamResource(userEntity.getProfilePhoto().getImageFile().getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(userEntity.getProfilePhoto().getImageFile().length())
                    .body(file);

    }


    //Post method for creating a new project
    @PostMapping("/createProject")
    public String createProject(Project project,
                                @RequestParam("file") MultipartFile[] files,
                                HttpServletRequest request) {

        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity query = checkQuery.get();
        project.setOwner(query);
        LocalDate date = LocalDate.now();
        project.setDate(date);

        for (MultipartFile file : files) {
            Image image = new Image(file);
            project.addImage(image);
        }

        ArrayList<Project> userProjects = new ArrayList<Project>(query.getProjects());
        userProjects.add(project);
        query.setProjects(userProjects);
        userService.saveUser(query);

        projectService.saveProject(project);
        return "redirect:/project-details/" + project.getId() + "/";
    }



    //Post method for writing a comment
    @PostMapping("/project-details/{id}/comment")
    String comment(@PathVariable Long id, Comment comment, HttpServletRequest request, Model model){

        Comment newComment = new Comment(comment.getText());
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();

        newComment.setProject(project);
        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity query = checkQuery.get();
        newComment.setUser(query);
        newComment.setUserName(query.getName());
        project.addComment(newComment);
        projectService.saveProject(project);

        return "redirect:/project-details/" + id + "/";
    }


    //Post method fot donating
    @PostMapping("/project-details/{id}/donate")
    String donate(@PathVariable Long id, int donation, HttpServletRequest request, Model model){

        Inversion newInversion = new Inversion(donation);
        LocalDate date = LocalDate.now();
        newInversion.setDate(date);
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();
        newInversion.setProject(project);
        Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity user = checkQuery.get();
        newInversion.setUser(user);
        project.addInversion(newInversion);
        projectService.saveProject(project);
        if(checkProject.get().getGoal() <= project.getCurrentAmount()){
            emailService.sendEmail(project.getOwner().getName(), project.getOwner().getEmail(),"Your project has reached its goal");
        }
        user.addInversion(newInversion);
        userService.saveUser(user);


        return "redirect:/project-details/" + id + "/";
    }


    //Get method for deleting project
    @GetMapping ("/project-details/{id}/delete")
    String deleteProject(@PathVariable Long id, HttpServletRequest request){
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();
        if (!project.getInversions().isEmpty())
            return "redirect:/error-page";

        if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())){
            commentRepository.deleteByProjectId(id);
            projectService.deleteProject(id);
        }

        return "redirect:/";
        }


        //Get method fot edit project page
    @GetMapping("/editProject/{id}")
    public String editProject(Model model, @PathVariable long id, HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        Optional<Project> project = projectService.getOptionalProject(id);
        Optional<UserEntity> user = userService.findUserByName(userName);
        if(user.isPresent() && project.isPresent() && (project.get().getOwner().equals(user.get()) || request.isUserInRole("ADMIN"))){
            model.addAttribute("isEditing", true);
            model.addAttribute("project", project.get());
            model.addAttribute("categories", Category.values());
            return "create-project";
        }
        return "error-page";

    }


    //Post method for editing an existing project
    @PostMapping("/editProject/{id}")
    public String replaceProject(@PathVariable long id, Project newProject,
                                 @RequestParam("file") MultipartFile[] files) {
        Optional<Project> project = projectService.getOptionalProject(id);
        if (project.isPresent()) {
            Project proj = project.get();
            if(!files[0].isEmpty() ){
                for (MultipartFile file : files) {
                    Image image = new Image(file);
                    proj.addImage(image);

                }
            }
            proj.setName(newProject.getName());
            proj.setDescription(newProject.getDescription());
            proj.setCategory(newProject.getCategory());
            proj.setUrl(newProject.getUrl());
            proj.setGoal(newProject.getGoal());

            projectService.saveProject(proj);
            }

        return "redirect:/project-details/" + id + "/";
    }


    @GetMapping("/comment/{projectId}/{id}/delete")
    String deleteComment(@PathVariable Long id, @PathVariable Long projectId, HttpServletRequest request) {
        Optional<Project> checkProject = projectService.getOptionalProject(projectId);
        if (checkProject == null)
            return "redirect:/error-page";

        if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(checkProject.get().getOwner().getName())) {
            Comment checkComment = commentRepository.findById(id).orElseThrow();
            if (checkComment == null)
                return "redirect:/error-page";

            Project project = checkProject.get();

            Optional<UserEntity> checkQuery = userService.findUserByName(request.getUserPrincipal().getName());
            if (checkQuery == null)
                return "redirect:/error-page";

            project.deleteComment(checkComment);
            commentService.deleteComment(id);
            projectService.saveProject(project);

            return "redirect:/project-details/" + projectId + "/";
        }
        return "redirect:/error-page";

    }


}
