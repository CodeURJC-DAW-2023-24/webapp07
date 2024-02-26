package com.daw.webapp07.controller;

import com.daw.webapp07.model.*;
import com.daw.webapp07.repository.*;
import com.daw.webapp07.service.DatabaseInitializer;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.sql.SQLException;
import java.util.*;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    InversionRepository inversionRepository;
    @GetMapping("/")
    public String innerPage(Model model, HttpServletRequest request, Pageable pageable) {
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
            if(user.isPresent() && user.get().hasInversions()){
                model.addAttribute("projects", recommendationSimple(user.get()));
                model.addAttribute("user", user);

            }

        }
        pageable = PageRequest.of(0, 6);
        model.addAttribute("projects", projectRepository.findAll(pageable));

        return "inner-page";
    }



    @GetMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id, HttpServletRequest request) {
        Project project = projectRepository.findById(id).orElseThrow();


        UserEntity user = userRepository.findByName(request.getUserPrincipal().getName()).orElseThrow();

        if  (user.getName().equals(project.getOwner().getName()) || request.isUserInRole("ADMIN")){
            model.addAttribute("privileged",true);
        }

        model.addAttribute("project", project);
        model.addAttribute("id", id);
        model.addAttribute("comment", new Comment());

        HashMap<String,Integer> donors = new HashMap<>();
        int total = 0;
        for(Inversion i: project.getInversions()){
            total+=i.getAmount();
            if(donors.containsKey(i.getUser().getName())){
                donors.put(i.getUser().getName(),donors.get(i.getUser().getName())+i.getAmount());
            }else{
                donors.put(i.getUser().getName(),i.getAmount());
            }
        }

        List<String> names = new ArrayList<>(donors.keySet());
        List<Integer> quantities = new ArrayList<>(donors.values());
        names.sort((a,b)->donors.get(b).compareTo(donors.get(a)));
        quantities.sort((a,b)->b.compareTo(a));
        System.out.println(names);
        System.out.println(quantities);
        System.out.println("gfwytgwygq3wyg3ry");

        model.addAttribute("donors",array_to_string_jsarray(names));
        model.addAttribute("quantities", array_to_int_jsarray(quantities));

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
        if(user.isPresent()){
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

        model.addAttribute(project);

        return "project-details";
    }


    @PostMapping("/project-details/{id}/comment")
    String comment(@PathVariable Long id, Comment comment, HttpServletRequest request, Model model){

        Comment newComment = new Comment(comment.getText());
        Project project = projectRepository.findById(id).orElseThrow();
        newComment.setProject(project);
        newComment.setUser(userRepository.findByName(request.getUserPrincipal().getName()).orElseThrow());
        project.addComment(newComment);
        projectRepository.save(project);

        return "redirect:/project-details/" + id + "/";
    }

    @PostMapping("/project-details/{id}/donate")
    String donate(@PathVariable Long id, int donation, HttpServletRequest request, Model model){

        Inversion newInversion = new Inversion(donation);
        Project project = projectRepository.findById(id).orElseThrow();
        newInversion.setProject(project);
        UserEntity user = userRepository.findByName(request.getUserPrincipal().getName()).orElseThrow();
        newInversion.setUser(user);
        project.addInversion(newInversion);
        projectRepository.save(project);
        user.addInversion(newInversion);
        userRepository.save(user);


        return "redirect:/project-details/" + id + "/";
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

    private String array_to_string_jsarray(List<String> list){
        String ar = "[";
        for(String s: list){
            ar += "'"+s+"',";
        }
        if (ar.length() > 1) {ar = ar.substring(0,ar.length()-1);}
        ar += "]";
        System.out.println(ar);
        return ar;
    }

    private String array_to_int_jsarray(List<Integer> list){
        String ar = "[";
        for(Integer i: list){
            ar += i+",";
        }
        if (ar.length() > 1) {ar = ar.substring(0,ar.length()-1);}
        ar += "]";
        System.out.println(ar);
        return ar;
    }

}
