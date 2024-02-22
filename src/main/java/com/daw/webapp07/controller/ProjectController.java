package com.daw.webapp07.controller;

import com.daw.webapp07.model.Category;
import com.daw.webapp07.model.Inversion;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.daw.webapp07.model.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String innerPage(Model model, HttpServletRequest request) {
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
            if(user.isPresent() && user.get().hasInversions()){
                model.addAttribute("projects", recommendationSimple(user.get()));
            }

        }

        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("user", request.isUserInRole("USER"));
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
        if (index < project.getImages().size()){
            Resource file = new InputStreamResource(project.getImages().get(index).getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(project.getImages().get(index).length())
                    .body(file);
        }
        return ResponseEntity.notFound().build();

    }



    @PostMapping("/create-project/new")
    public String createBook(@RequestBody Project project, Model model) {
        projectRepository.save(project);
        return "inner-page";
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
