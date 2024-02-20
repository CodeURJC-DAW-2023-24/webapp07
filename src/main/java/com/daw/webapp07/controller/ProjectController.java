package com.daw.webapp07.controller;

import com.daw.webapp07.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.daw.webapp07.model.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/")
    public String innerPage(Model model) {

        model.addAttribute("projects", projectRepository.findAll());
        return "inner-page";
    }

    @GetMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("project",project.get());
        return "project-details";
    }

    @PostMapping("/create-project/new")
    public String createBook(@RequestBody Project project, Model model) {
        projectRepository.save(project);
        return "inner-page";
    }
}
