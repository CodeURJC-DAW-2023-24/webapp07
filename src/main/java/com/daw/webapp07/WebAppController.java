package com.daw.webapp07;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Controller
public class WebAppController {

    HashMap<String, Project> portfolio;
    public WebAppController(){
        portfolio = new HashMap<>();
        Project p = new Project("1", "Proyecto 1", "Descripcion 1", "Owner 1");
        portfolio.put("1", p);
    }



    @GetMapping("/index")
    public String index(Model model) {
        //model.addAttribute("nombre", "Mundo");
        return "index";
    }

    @GetMapping("/inner-page")
    public String innerPage(Model model) {
        model.addAttribute("projects", portfolio.values());
        return "inner-page";
    }

    @RequestMapping("/portfolio-datails/{id}")
    public String home(Model model, @PathVariable String id) {
        model.addAttribute("project", portfolio.get(id));
        return "/portfolio-details";
    }




}
