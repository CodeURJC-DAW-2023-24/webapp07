package com.daw.webapp07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String landing() {
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
