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
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

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

    //This method will load the main page, differentiating the logged users from the guests.
    //Apart from executing the recommendation algorithm
    @GetMapping("/")
    public String innerPage(Model model, HttpServletRequest request) {
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
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
            Optional<UserEntity> user = userRepository.findByName(principal.getName());
            if (user.isPresent()) {
                model.addAttribute("projects", projectService.searchRecommendedProjects(page,size,user.get().getId()));
            }
            return "portfolio";
        }
    }



    //Shows more details about the project, differentiating between logged users, guest and admin
    @GetMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id, HttpServletRequest request) {
        Optional<Project> checkProject = projectRepository.findById(id);
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
        Project project = projectRepository.findById(id).orElseThrow();
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
        UserEntity userEntity = userRepository.findById(id).orElseThrow();
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

        Optional<UserEntity> checkQuery = userRepository.findByName(request.getUserPrincipal().getName());
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

        projectRepository.save(project);
        return "redirect:/project-details/" + project.getId() + "/";
    }



    //Post method for writing a comment
    @PostMapping("/project-details/{id}/comment")
    String comment(@PathVariable Long id, Comment comment, HttpServletRequest request, Model model){

        Comment newComment = new Comment(comment.getText());
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();

        newComment.setProject(project);
        Optional<UserEntity> checkQuery = userRepository.findByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity query = checkQuery.get();
        newComment.setUser(query);
        project.addComment(newComment);
        projectRepository.save(project);

        return "redirect:/project-details/" + id + "/";
    }


    //Post method fot donating
    @PostMapping("/project-details/{id}/donate")
    String donate(@PathVariable Long id, int donation, HttpServletRequest request, Model model){

        Inversion newInversion = new Inversion(donation);
        LocalDate date = LocalDate.now();
        newInversion.setDate(date);
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();
        newInversion.setProject(project);
        Optional<UserEntity> checkQuery = userRepository.findByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity user = checkQuery.get();
        newInversion.setUser(user);
        project.addInversion(newInversion);
        projectRepository.save(project);
        if(checkProject.get().getGoal() <= project.getCurrentAmount()){
            emailService.sendEmail(project.getOwner().getName(), project.getOwner().getEmail(),"Your project has reached its goal");
        }
        user.addInversion(newInversion);
        userRepository.save(user);


        return "redirect:/project-details/" + id + "/";
    }


    //Get method for deleting project
    @GetMapping ("/project-details/{id}/delete")
    String deleteProject(@PathVariable Long id, HttpServletRequest request){
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();
        if (!project.getInversions().isEmpty())
            return "redirect:/error-page";

        if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())){
            commentRepository.deleteByProjectId(id);
            projectRepository.deleteById(id);
        }

        return "redirect:/";
        }


        //Get method fot edit project page
    @GetMapping("/editProject/{id}")
    public String editProject(Model model, @PathVariable long id, HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        Optional<Project> project = projectRepository.findById(id);
        Optional<UserEntity> user = userRepository.findByName(userName);
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
        Optional<Project> project = projectRepository.findById(id);
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

            projectRepository.save(proj);
            }

        return "redirect:/project-details/" + id + "/";
    }


    //Methods used for the reccomendation algorithm
    private List<Pair<Float,UserEntity>> getSimilarUsers(UserEntity user, HashMap<UserEntity,HashMap<Category,Float>> percentages){
        HashMap<Category, Float> base = percentages.get(user);
        List<Pair<Float,UserEntity>> similar = new ArrayList<>();
        for(UserEntity u: percentages.keySet()){
            if(u.equals(user)){
                continue;
            }
            float points = 0;
            for(Category c: Category.values()){
                points+=Math.abs(base.get(c)-percentages.get(u).get(c));
            }
            similar.add(new Pair<>(points,u));
        }
        similar.sort((a,b)->a.a.compareTo(b.a));
        return similar;
    }

    private HashMap<UserEntity,HashMap<Category,Float>> getPercentages(){
        HashMap<UserEntity,HashMap<Category,Float>> ups = new HashMap<>();
        for(UserEntity u: userRepository.findAll()) {
            HashMap<Category,Float> up = new HashMap<>();
            float total = 0;
            for(Category c: Category.values()){
                up.put(c,0f);
            }
            for(Inversion i: u.getInversions()){
                up.put(i.getProject().getCategory(),up.get(i.getProject().getCategory())+i.getAmount());
                total+=i.getAmount();
            }
            for(Category c: Category.values()){
                up.put(c,up.get(c)/total);
            }
                ups.put(u,up);
        }
        return ups;
    }

    private List<Project> recommendationSimple(UserEntity user){
        HashMap<UserEntity,HashMap<Category,Float>> percentages = getPercentages();
        List<Pair<Float, UserEntity>> users = getSimilarUsers(user, percentages);
        List<Project> projects = new ArrayList<>();
        HashSet<Project> set = new HashSet<>();
        for(Inversion i: user.getInversions()){
            set.add(i.getProject());
        }
        for(Pair<Float,UserEntity> p: users){
            for(Inversion i: p.b.getInversions()){
                if(!set.contains(i.getProject())){
                    projects.add(i.getProject());
                    set.add(i.getProject());
                }
            }
        }
        return projects;
    }

    private List<Project> likelihoodOfDonation(UserEntity user){
        throw new UnsupportedOperationException("Not implemented yet");
    }



}
