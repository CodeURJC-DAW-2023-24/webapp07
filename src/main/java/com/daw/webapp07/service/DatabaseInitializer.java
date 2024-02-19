package com.daw.webapp07.service;


import com.daw.webapp07.model.Project;
import com.daw.webapp07.repository.InversionRepository;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.daw.webapp07.security.WebSecurityConfig;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private InversionRepository inversionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Project p = new Project("Proyecto 1", "Descripcion 1", "Owner 1");
        p.addImage("/img/kf/OIG2.jpg");
        p.addImage("/img/kf/OIG4.jpg");
        p.addImage("/img/kf/MENU-DONER-KEBAP-7.jpg");
        projectRepository.save(p);
    }

}
