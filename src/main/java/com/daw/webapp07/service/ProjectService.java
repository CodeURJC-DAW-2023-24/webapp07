package com.daw.webapp07.service;

import com.daw.webapp07.model.Project;
import com.daw.webapp07.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;


    public Page<Project> searchProjects(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return  projectRepository.findAll(pageable);
    }

   /*public Page<Project> searchRecommendedProjects(int page, int size, User user)
    {
        Pageable pageable = PageRequest.of(page, size);
        return  projectRepository.projectRepository.recomendedProjects(pageable);
    }*/

}