package com.daw.webapp07.controller;

import com.daw.webapp07.model.Project;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.service.PdfService;
import com.daw.webapp07.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Optional;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfGenerationService;


    @Autowired
    ProjectService projectService;

    // This method is called when the user tries to generate a pdf
    @GetMapping("/project-details/{id}/generate-pdf")
    public void generatePdf(@PathVariable long id, HttpServletResponse response) throws IOException {
        Optional<Project> project = projectService.getOptionalProject(id);
        if (project.isPresent()) {
            pdfGenerationService.generatePdf(project.get(), response, id);
        }
    }
}