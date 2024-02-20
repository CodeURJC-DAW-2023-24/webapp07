package com.daw.webapp07.service;


import com.daw.webapp07.model.Category;
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

import java.util.ArrayList;

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
        Project p = new Project(
                "KebabFinder",
                "Kebab Finder es una aplicación que te permite encontrar los mejores kebabs de tu ciudad. Podrás ver las opiniones de otros usuarios, ver la carta de los kebabs y hacer tus propias reseñas. Además, podrás solicitar un kebab a domicilio y pagar con tarjeta.",
                "MarkiIndustries",
                "28 January, 2024",
                Category.Technology,
                "https://kebabfinder.com",
                "/img/kf/OIG2.jpg","/img/kf/OIG4.jpg","/img/kf/MENU-DONER-KEBAP-7.jpg",
                150000,
                0,
                new ArrayList<>());
        projectRepository.save(p);
    }

}
