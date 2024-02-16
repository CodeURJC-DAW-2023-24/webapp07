package com.daw.webapp07;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "header";
    }

    @PostMapping("/login")
    public String loginSubmit(Model model, User user) {
        System.out.println(user.getName());
        return "inner-page";
    }


}
