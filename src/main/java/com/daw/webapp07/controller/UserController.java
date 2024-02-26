package com.daw.webapp07.controller;

import com.daw.webapp07.model.Category;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import com.daw.webapp07.service.RepositoryUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("newUser", new UserEntity());
        return "signup";
    }

    @PostMapping("/signup")
    public String newsignup(UserEntity user, Model model) {
        //encode password
        user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
        user.setRoles(List.of("USER"));
        userDetailsService.registerUser(user);
        return "inner-page";
    }

    @GetMapping("/landing-page")
    public String landing(Model model,  HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userRepository.findByName(userName);
        model.addAttribute("projects", projectRepository.findAll());
        return "landing-page";
    }

    @GetMapping("/createProject/")
    public String createProject(Model model, HttpServletRequest request){
        String userName = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userRepository.findByName(userName);
        if(user.isPresent()){
            model.addAttribute("project", new Project());
            model.addAttribute("categories", Category.values());

            return "create-project";
        }
        return "login";

    }

    @GetMapping("/myProjects")
    public String myProjects(Model model, HttpServletRequest request){
        String userName = request.getUserPrincipal().getName();
        List<Project> userProjects = projectRepository.findByOwnerName(userName);
        model.addAttribute("projects", userProjects);
        Optional<UserEntity> user = userRepository.findByName(userName);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            model.addAttribute("id", user.get().getId()); //profile photo needs id
        }
        return "myProjects";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }
}
