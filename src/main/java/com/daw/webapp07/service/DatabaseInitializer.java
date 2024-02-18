package com.daw.webapp07.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class DatabaseInitializer {

    /*
    @Autowired
    private UserRepository userRepository;
    */

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        // Sample projects


       // bookRepository.save(new Book("SUEÑOS DE ACERO Y NEON",
        //         "Los personajes que protagonizan este relato sobreviven en una sociedad en decadencia a la que, no obstante, lograrán devolver la posibilidad de un futuro. Año 2484. En un mundo dominado por las grandes corporaciones, solo un hombre, Jordi Thompson, detective privado deslenguado y vividor, pero de gran talento y sentido d..."));

        // Sample users

        /*
        userRepository.save(new User("user1", passwordEncoder.encode("1234"), "USER"));
        userRepository.save(new User("user2", passwordEncoder.encode("1234"), "USER"));

         */
    }

}
