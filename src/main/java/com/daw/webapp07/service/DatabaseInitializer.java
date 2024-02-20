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


        p = new Project(
                "Smart Garden",
                "Un sistema automatizado de jardinería que utiliza sensores para monitorear las condiciones del suelo y las plantas, ajustando automáticamente el riego y la iluminación para un crecimiento óptimo.",
                "MortezLab",
                "20 February, 2024",
                Category.Technology,
                "https://smartgarden.com",
                "/img/smartgarden/smartgarden1.jpg", "/img/smartgarden/smartgarden2.jpg", "/img/smartgarden/smartgarden3.jpg",
                7000,
                0,
                new ArrayList<>());
        projectRepository.save(p);

        p = new Project(
                "EcoBike: La bicicleta eléctrica todo terreno",
                "Una bicicleta eléctrica diseñada para todo tipo de terrenos, desde montañas hasta calles urbanas. Equipada con baterías de larga duración y un motor potente, EcoBike busca revolucionar la movilidad sostenible.",
                "ecoworld",
                "2 February, 2024",
                Category.Entertainment,
                "https://ecobike.com",
                "/img/ecobike/ecobike1.jpg", "/img/ecobike/ecobike2.jpg", "/img/ecobike/ecobike3.jpg",
                12000,
                0,
                new ArrayList<>());
        projectRepository.save(p);

        p = new Project(
                "CodeLearn",
                "Una plataforma educativa en línea que ofrece cursos interactivos para aprender programación, desde conceptos básicos hasta habilidades avanzadas en varios lenguajes de programación.",
                "proglearn",
                "16 January, 2024",
                Category.Education,
                "https://proglearn.com",
                "/img/proglearn/proglearn1.jpg", "/img/proglearn/proglearn2.jpg", "/img/proglearn/proglearn3.jpg",
                12000,
                0,
                new ArrayList<>());
        projectRepository.save(p);

        p = new Project(
                "Trustphone",
                "El teléfono del que te puedes fiar, funcionamiento perfecto y fácilmente reparable",
                "Trustbusiness",
                "5 January, 2024",
                Category.Technology,
                "https://trustbusiness.com",
                "/img/trustphone/trustphone1.jpg", "/img/trustphone/trustphone2.jpg", "/img/trustphone/trustphone3.jpg",
                50000,
                0,
                new ArrayList<>());
        projectRepository.save(p);


        p = new Project(
                "Purewater",
                "Un dispositivo compacto y portátil que utiliza tecnología de filtración avanzada para purificar el agua de fuentes no potables, haciéndola segura para beber en situaciones de emergencia o actividades al aire libre.",
                "Bewater",
                "5 February, 2024",
                Category.Health,
                "https://bewater.com",
                "/img/bewater/trustphone1.jpg", "/img/trustphone/trustphone2.jpg", "/img/trustphone/trustphone3.jpg",
                50000,
                0,
                new ArrayList<>());
        projectRepository.save(p);


    }

}
