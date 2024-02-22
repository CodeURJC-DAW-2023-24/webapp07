package com.daw.webapp07.controller;

import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
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
    public String newsignup(UserEntity user) {
        user.setRoles(List.of("USER"));
        userRepository.save(user);
        //controlar error de repetición de usuarios
        return "inner-page";
    }


    @GetMapping("/landing-page")
    public String landing(Model model, HttpServletRequest request) {

        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("user", request.isUserInRole("USER"));

        return "landing-page";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }


    // Hacer que esto funcione
    @GetMapping("/new-proyect")
    public String newproyect() {
        return "create-proyect";
    }


}
