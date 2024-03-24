package com.daw.webapp07.controller.REST;

import com.daw.webapp07.DTO.GraphicsDTO;
import com.daw.webapp07.DTO.RankingDTO;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.service.GraphicsService;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.RankingService;
import com.daw.webapp07.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GraphicsRestController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private GraphicsService graphicsService;

    @Autowired
    private RankingService rankingService;

    @GetMapping("/projects/{id}/graphics")
    public ResponseEntity<GraphicsDTO> getProjectGraphics(@PathVariable long id) {
        Optional<Project> checkProject = projectService.getOptionalProject(id);
        if (checkProject.isPresent()) {
            Project project = checkProject.get();
            graphicsService.initializeWith(project);
            GraphicsDTO graphicsDTO = new GraphicsDTO(graphicsService.getPastmoney(),graphicsService.getTimes(),graphicsService.getQuantities(),graphicsService.getNames());
            return new ResponseEntity<>(graphicsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rankings")
    public ResponseEntity<RankingDTO> getRankings() {
        List<UserEntity> users = rankingService.getTopInvestors();
        List<Project> projects = rankingService.getTopProjects();
        RankingDTO rankingDTO = new RankingDTO(users, projects);
        return new ResponseEntity<>(rankingDTO, HttpStatus.OK);
    }
}
