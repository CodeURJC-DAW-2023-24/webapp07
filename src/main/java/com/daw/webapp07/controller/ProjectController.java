package com.daw.webapp07.controller;

import com.daw.webapp07.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.daw.webapp07.model.Project;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping(value={"/inner-page","/"})
    public String innerPage(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "inner-page";
    }

    @RequestMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("project",project.get());
        return "project-details";
    }




}
