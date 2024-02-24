package com.daw.webapp07.controller;

import com.daw.webapp07.model.*;
import com.daw.webapp07.repository.ImageRepository;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import com.daw.webapp07.service.DatabaseInitializer;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@Controller
public class ProjectController {

    @Autowired
    private DatabaseInitializer dbinit;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/")
    public String innerPage(Model model, HttpServletRequest request) {
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
            if(user.isPresent() && user.get().hasInversions()){
                model.addAttribute("projects", recommendationSimple(user.get()));
                model.addAttribute("user", user);

            }

        }

        model.addAttribute("projects", projectRepository.findAll());

        return "inner-page";
    }



    @GetMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id, HttpServletRequest request) {
        Project project = projectRepository.findById(id).orElseThrow();
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
            if(user.isPresent() && user.get().hasInversions()){
                model.addAttribute("projects", recommendationSimple(user.get()));
            }

        }

        model.addAttribute("project", project);
        model.addAttribute("user", request.isUserInRole("USER"));

        return "project-details";
    }

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

    @GetMapping("/users/{id}/profile")
    public ResponseEntity<Object> displayProfilePhoto(@PathVariable Long id) throws SQLException{
        UserEntity userEntity = userRepository.findById(id).orElseThrow();
        Resource file = new InputStreamResource(userEntity.getProfilePhoto().getImageFile().getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(userEntity.getProfilePhoto().getImageFile().length())
                    .body(file);


    }

    @GetMapping("/editProfile/{id}")
    public String editProfile(Model model, HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userRepository.findByName(userName);
        model.addAttribute("user", request.isUserInRole("USER"));
        if(user.isPresent()){
            model.addAttribute("id", user.get().getId()); //profile photo needs id
            model.addAttribute("userEntity", user.get());
        }
        return "editProfile";
    }

    @PostMapping("/editProfile/{id}")
    public String updateProfile(Model model, @PathVariable Long id,  UserEntity userEntity, HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userRepository.findByName(name);
        if (user.isPresent() && user.get().getId() == id) {
            user.get().setName(userEntity.getName());
            user.get().setEmail(userEntity.getEmail());
            if (userEntity.getProfilePhoto() != null) {
                user.get().setProfilePhoto(userEntity.getProfilePhoto());
            }
            userRepository.save(user.get());

        }
        model.addAttribute("user", request.isUserInRole("USER"));
        model.addAttribute("id", id); //profile photo needs id
        return "landing-page";
    }




    @PostMapping("/newProject")
    public String createProject(Project project, Model model, HttpServletRequest request) {

        project.setOwner(userRepository.findByName(request.getUserPrincipal().getName()).orElseThrow());
        Calendar c = Calendar.getInstance();
        String day = Integer.toString(c.get(Calendar.DATE));
        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        String year = Integer.toString(c.get(Calendar.YEAR));
        project.setDate(day + "/" + month + "/" + year);
        projectRepository.save(project);


        String userName = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userRepository.findByName(userName);
        model.addAttribute("user", request.isUserInRole("USER"));
        if(user.isPresent()){
            model.addAttribute("id", user.get().getId()); //profile photo needs id
        }
        model.addAttribute(project);

        return "project-details";
    }






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
