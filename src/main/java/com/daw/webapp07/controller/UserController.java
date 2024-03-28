package com.daw.webapp07.controller;

import com.daw.webapp07.model.Category;
import com.daw.webapp07.model.Image;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.RankingService;
import com.daw.webapp07.service.RepositoryUserDetailsService;
import com.daw.webapp07.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {


    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    // This method is called when the user tries to access the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // This method is called when the user tries to access the signup page
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("newUser", new UserEntity());
        return "signup";
    }

    // This method is called when the user submits the signup form
    @PostMapping("/signup")
    public String newsignup(UserEntity user, Model model) {
        //encode password
        user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
        user.setRoles(List.of("USER"));
        if (userDetailsService.registerUser(user)){
            return "login";
        }

        return "error-page";
    }

    // This method is called when the user tries to access the create project page
    @GetMapping("/createProject")
    public String createProject(Model model, HttpServletRequest request){
        String userName = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userService.findUserByName(userName);
        if(user.isPresent()){
            model.addAttribute("isEditing", false);
            model.addAttribute("project", new Project());
            model.addAttribute("categories", Category.values());
            return "create-project";
        }
        return "login";

    }

    // This method is called when the user submits the create project form
    @GetMapping("/myProjects")
    public String myProjects(Model model, HttpServletRequest request){
        String userName = request.getUserPrincipal().getName();
        List<Project> userProjects = projectService.findByOwnerName(userName);
        model.addAttribute("projects", userProjects);
        Optional<UserEntity> user = userService.findUserByName(userName);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            model.addAttribute("id", user.get().getId()); //profile photo needs id
        }
        return "myProjects";
    }

    // This method is called when the user tries to access the edit project page
    @GetMapping("/editProfile")
    public String editProfile(Model model, HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userService.findUserByName(userName);
        if(user.isPresent()){
            model.addAttribute("userEntity", user.get());
        }
        return "editProfile";
    }

    // This method is called when the user submits the edit project form
    @PostMapping("/editProfile")
    public String updateProfile(Model model, UserEntity userEntity,
                                HttpServletRequest request,
                                @RequestParam("photo") MultipartFile profilePhoto) {
        String name = request.getUserPrincipal().getName();
        Optional<UserEntity> user = userService.findUserByName(name);
        if (user.isPresent()) {
            user.get().setEmail(userEntity.getEmail());
            if (!profilePhoto.isEmpty()) {
                user.get().setProfilePhoto(new Image(profilePhoto));
            }
            userService.saveUser(user.get());
        }
        return "redirect:/";
    }

    // This method is called when the user tries to access the ranking page
    @GetMapping("/ranking")
    public String ranking(Model model, HttpServletRequest request) {
        List<UserEntity> users = rankingService.getTopInvestors();
        model.addAttribute("inv1", users.get(0));
        model.addAttribute("inv2", users.get(1));
        model.addAttribute("inv3", users.get(2));
        model.addAttribute("inv1am", users.get(0).getTotalInvested());
        model.addAttribute("inv2am", users.get(1).getTotalInvested());
        model.addAttribute("inv3am", users.get(2).getTotalInvested());
        List<Project> projects = rankingService.getTopProjects();
        model.addAttribute("pro1", projects.get(0).getName());
        model.addAttribute("pro2", projects.get(1).getName());
        model.addAttribute("pro3", projects.get(2).getName());
        model.addAttribute("pro1am", projects.get(0).getCurrentAmount());
        model.addAttribute("pro2am", projects.get(1).getCurrentAmount());
        model.addAttribute("pro3am", projects.get(2).getCurrentAmount());
        return "ranking";
    }
}