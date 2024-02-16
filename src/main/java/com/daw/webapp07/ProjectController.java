package com.daw.webapp07;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class ProjectController {

    HashMap<String, Project> portfolio;
    public ProjectController(){
        portfolio = new HashMap<>();
        Project p = new Project("1", "Proyecto 1", "Descripcion 1", "Owner 1");
        p.addImage("/img/kf/OIG2.jpg");
        p.addImage("/img/kf/OIG4.jpg");
        p.addImage("/img/kf/MENU-DONER-KEBAP-7.jpg");

        portfolio.put("1", p);
    }



    @GetMapping("/index")
    public String index(Model model) {
        //model.addAttribute("nombre", "Mundo");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        //model.addAttribute()
        return "login";
    }
    @GetMapping("/inner-page")
    public String innerPage(Model model) {
        model.addAttribute("projects", portfolio.values());
        return "inner-page";
    }

    @RequestMapping("/project-details/{id}")
    public String home(Model model, @PathVariable String id) {
        model.addAttribute("project", portfolio.get(id));
        return "project-details";
    }




}
