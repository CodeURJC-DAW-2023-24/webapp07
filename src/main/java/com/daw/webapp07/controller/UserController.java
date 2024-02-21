package com.daw.webapp07.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/landing-page")
    public String landing(Model model, HttpServletRequest request) {
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
