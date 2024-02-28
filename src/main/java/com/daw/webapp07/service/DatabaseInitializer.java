package com.daw.webapp07.service;


import com.daw.webapp07.model.*;
import com.daw.webapp07.repository.InversionRepository;
import com.daw.webapp07.repository.ProjectRepository;
import com.daw.webapp07.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import jakarta.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DatabaseInitializer {
    @Value("${security.user}")
    private String admin;

    @Value("${security.encodedPassword}")
    private String adminpass;
    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InversionRepository inversionRepository;

    @PostConstruct
    public void init() {
        UserEntity u1 = createAndSaveUser("User1","user1@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/profiles/user1.png","USER");
        UserEntity u2 = createAndSaveUser("User2","user2@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/profiles/user2.png","USER");
        UserEntity u3 = createAndSaveUser("User3","user3@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/profiles/user3.jpg","USER");
        UserEntity u4 = createAndSaveUser("User4","user4@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/profiles/user1.png","USER");
        UserEntity adm = createAndSaveUser("admin",admin, adminpass, FILES_FOLDER + "/profiles/admin.png","USER","ADMIN");

        UserEntity MarkiIndustries = createAndSaveUser("MarkiIndustries","markiindustries@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/kf/OIG2.jpg","USER");
        Project KebabFinder = createAndSaveProject(
                "KebabFinder",
                "Kebab Finder es una aplicación que te permite encontrar los mejores kebabs de tu ciudad. Podrás ver las opiniones de otros usuarios, ver la carta de los kebabs y hacer tus propias reseñas. Además, podrás solicitar un kebab a domicilio y pagar con tarjeta.",
                MarkiIndustries,
                "28 January, 2022",
                Category.Technology,
                "https://kebabfinder.com",
                150000,
                0,
                FILES_FOLDER + "/kf/OIG2.jpg", FILES_FOLDER + "/kf/OIG4.jpg", FILES_FOLDER + "/kf/MENU-DONER-KEBAP-7.jpg"
        );
        Inversion inv1 = createAndSaveInversion(u1, KebabFinder, 1000, LocalDate.of(2023, 12, 2));
        Inversion inv2 = createAndSaveInversion(u2, KebabFinder,2000, LocalDate.of(2023, 1, 23));
        Inversion inv3 = createAndSaveInversion(u3, KebabFinder,3000, LocalDate.of(2023, 6, 30));
        Inversion inv4 = createAndSaveInversion(u4, KebabFinder,4000, LocalDate.of(2023, 4, 25));

        UserEntity ecoworld = createAndSaveUser("EcoWorld","ecowrld@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/ecobike/ecobike1.jpg","USER");
        Project ecobike = createAndSaveProject(
                "EcoBike: La bicicleta eléctrica todo terreno",
                "Una bicicleta eléctrica diseñada para todo tipo de terrenos, desde montañas hasta calles urbanas. Equipada con baterías de larga duración y un motor potente, EcoBike busca revolucionar la movilidad sostenible.",
                ecoworld,
                "2 February, 2024",
                Category.Entertainment,
                "https://ecobike.com",
                12000,
                0,
                FILES_FOLDER + "/ecobike/ecobike1.jpg", FILES_FOLDER + "/ecobike/ecobike2.jpg", FILES_FOLDER + "/ecobike/ecobike3.jpg"
        );

        Inversion inv5 = createAndSaveInversion(u1, ecobike, 1000, LocalDate.of(2023, 12, 2));

        UserEntity proglearn = createAndSaveUser("proglearn","progolo@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/proglearn/proglearn1.jpeg","USER");
        Project CodeLearn = createAndSaveProject(
                "CodeLearn",
                "Una plataforma educativa en línea que ofrece cursos interactivos para aprender programación, desde conceptos básicos hasta habilidades avanzadas en varios lenguajes de programación.",
                proglearn,
                "16 January, 2024",
                Category.Education,
                "https://proglearn.com",
                12000,
                0,
                FILES_FOLDER + "/proglearn/proglearn1.jpeg", FILES_FOLDER + "/proglearn/proglearn3.jpg"
        );

        UserEntity Trustbusiness = createAndSaveUser("Trustbusiness","trustbusiness@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/trustphone/trustphone1.jpg","USER");
        Project Trustphone = createAndSaveProject(
                "Trustphone",
                "El teléfono del que te puedes fiar, funcionamiento perfecto y fácilmente reparable",
                Trustbusiness,
                "5 January, 2024",
                Category.Technology,
                "https://trustbusiness.com",
                50000,
                0,
                FILES_FOLDER + "/trustphone/trustphone1.jpg", FILES_FOLDER + "/trustphone/trustphone2.jpg", FILES_FOLDER + "/trustphone/trustphone3.jpg"
        );

        UserEntity Bewater = createAndSaveUser("Bewater","bewater@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/purewater/purewater1.png","USER");
        Project Purewater = createAndSaveProject(
                "Purewater",
                "Un dispositivo compacto y portátil que utiliza tecnología de filtración avanzada para purificar el agua de fuentes no potables, haciéndola segura para beber en situaciones de emergencia o actividades al aire libre.",
                Bewater,
                "5 February, 2024",
                Category.Health,
                "https://bewater.com",
                50000,
                0,
                FILES_FOLDER + "/purewater/purewater1.png", FILES_FOLDER + "/purewater/purewater2.jpeg", FILES_FOLDER + "/purewater/purewater3.jpeg"
        );

        UserEntity MortezLab = createAndSaveUser("MortezLab","moctezumez@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/smartgarden/smartgarden1.jpg","USER");
        Project SmartGarden = createAndSaveProject(
                "Smart Garden",
                "Un sistema automatizado de jardinería que utiliza sensores para monitorear las condiciones del suelo y las plantas, ajustando automáticamente el riego y la iluminación para un crecimiento óptimo.",
                MortezLab,
                "20 February, 2024",
                Category.Technology,
                "https://smartgarden.com",
                7000,
                0,
                FILES_FOLDER + "/smartgarden/smartgarden1.jpg", FILES_FOLDER + "/smartgarden/smartgarden2.jpg", FILES_FOLDER + "/smartgarden/smartgarden3.jpg"
        );

        UserEntity PixelStudios = createAndSaveUser("PixelStudios","pixel@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/pixelquest/pixelquest1.jpeg","USER");
        Project PixelQuest = createAndSaveProject(
                "PixelQuest",
                "Un juego de aventuras en 2D con gráficos pixelados. Explora mundos fascinantes, resuelve acertijos y enfréntate a desafíos emocionantes en este juego indie desarrollado por un pequeño equipo apasionado.",
                PixelStudios,
                "5 August, 2024",
                Category.Gaming,
                "https://pixelquest.com",
                18000,
                0,
                FILES_FOLDER + "/pixelquest/pixelquest1.jpeg"
        );

        Inversion inv6 = createAndSaveInversion(u3, PixelQuest, 1000, LocalDate.of(2023, 12, 2));

        UserEntity MindCare = createAndSaveUser("MindCare","mindcare@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/mindwell/mindwell1.jpeg","USER");
        Project MindWell = createAndSaveProject(
                "MindWell",
                "Una plataforma en línea que ofrece recursos y apoyo para la salud mental. Proporciona acceso a terapeutas, herramientas de meditación y comunidades de apoyo para mejorar el bienestar emocional.",
                MindCare,
                "12 July, 2024",
                Category.Health,
                "https://mindwell.com",
                25000,
                0,
                FILES_FOLDER + "/mindwell/mindwell1.jpeg", FILES_FOLDER + "/mindwell/mindwell2.png"
        );

        UserEntity LingoTech = createAndSaveUser("LingoTech","Lingotech@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/lingolearn/lingolearn1.jpeg","USER");
        Project LingoLearn = createAndSaveProject(
                "LingoLearn",
                "Una plataforma interactiva para aprender idiomas de manera eficaz. Ofrece cursos personalizados, práctica de conversación y seguimiento del progreso para ayudar a los usuarios a alcanzar la fluidez.",
                LingoTech,
                "22 September, 2024",
                Category.Education,
                "https://lingolearn.com",
                15000,
                0,
                FILES_FOLDER + "/lingolearn/lingolearn1.jpeg", FILES_FOLDER + "/lingolearn/lingolearn2.jpeg", FILES_FOLDER + "/lingolearn/lingolearn3.png"
        );

        UserEntity GreenHarbor = createAndSaveUser("GreenHarbor","greenharbor@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/urbanharvest/urbanharvest1.jpeg","USER");
        Project UrbanHarvest = createAndSaveProject(
                "UrbanHarvest",
                "Un sistema de agricultura urbana que permite a las comunidades cultivar alimentos frescos localmente. Utiliza tecnologías sostenibles para maximizar la producción en entornos urbanos.",
                GreenHarbor,
                "10 October, 2024",
                Category.Sustainability,
                "https://urbanharvest.com",
                22000,
                0,
                FILES_FOLDER + "/urbanharvest/urbanharvest1.jpeg", FILES_FOLDER + "/urbanharvest/urbanharvest2.jpeg", FILES_FOLDER + "/urbanharvest/urbanharvest3.jpeg"
        );

        UserEntity CosmicTech = createAndSaveUser("CosmicTech","cosmictech@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/galaxygetaway/galaxygetaway1.jpeg","USER");
        Project GalaxyGetaway = createAndSaveProject(
                "GalaxyGetaway",
                "Explora el espacio exterior con esta aplicación que ofrece viajes espaciales virtuales. Descubre planetas lejanos, participa en misiones espaciales simuladas y aprende sobre el universo.",
                CosmicTech,
                "15 November, 2024",
                Category.Technology,
                "https://galaxygetaway.com",
                30000,
                0,
                FILES_FOLDER + "/galaxygetaway/galaxygetaway1.jpeg", FILES_FOLDER + "/galaxygetaway/galaxygetaway2.jpeg"
        );

        UserEntity LiveMusicEnt = createAndSaveUser("LiveMusicEnt","LiveMusicEnt@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/livebeat/liveBeat1.jpeg","USER");
        Project LiveBeat = createAndSaveProject(
                "LiveBeat",
                "Disfruta de conciertos en vivo desde la comodidad de tu hogar. LiveBeat ofrece acceso a actuaciones exclusivas de artistas populares y emergentes, todo en alta definición y sonido envolvente.",
                LiveMusicEnt,
                "18 January, 2025",
                Category.Entertainment,
                "https://livebeat.com",
                25000,
                0,
                FILES_FOLDER + "/livebeat/liveBeat1.jpeg"
        );

        UserEntity WellnessHub = createAndSaveUser("WellnessHub","wellnesHub@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/nutrifuel/nutrifuel1.jpeg", "USER");
        Project NutriFuel = createAndSaveProject(
                "NutriFuel",
                "Fomentando hábitos alimenticios saludables con recetas nutritivas y planes de comidas personalizados. NutriFuel ayuda a las personas a alcanzar sus objetivos de bienestar a través de una alimentación equilibrada.",
                WellnessHub,
                "7 February, 2025",
                Category.Health,
                "https://nutrifuel.com",
                18000,
                0,
                FILES_FOLDER + "/nutrifuel/nutrifuel1.jpeg", FILES_FOLDER + "/nutrifuel/nutrifuel2.jpeg"
        );

        Project BerniBear = createAndSaveProject(
                "Berni the Bear Season 3",
                "The proposal seeks funding for the third season of \"Bernie Bear,\". Season three will showcase captivating storylines, character development, and stunning animation, emphasizing themes of friendship, perseverance, and environmental stewardship.",
                u1,
                "30 November, 2024",
                Category.Entertainment,
                "https://www.youtube.com/user/bernardbear",
                150000,
                0,
                FILES_FOLDER + "/berni/berni2.jpg", FILES_FOLDER + "/berni/berni3.jpg"
        );

        Inversion inversion1 = createAndSaveInversion(
                u1,
                KebabFinder,
                10000,
                LocalDate.of(2023, 3, 15)
        );
        Inversion inversion2 = createAndSaveInversion(
                u2,
                KebabFinder,
                5250,
                LocalDate.of(2023, 5, 10)
        );
        Inversion inversion3 = createAndSaveInversion(
                u3,
                KebabFinder,
                12000,
                LocalDate.of(2023, 7, 17)
        );
        Inversion inversion4 = createAndSaveInversion(
                u4,
                KebabFinder,
                20000,
                LocalDate.of(2023, 8, 11)
        );
        Inversion inversion5 = createAndSaveInversion(
                u1,
                KebabFinder,
                7500,
                LocalDate.of(2023, 11, 9)
        );
    }

    private Project createAndSaveProject(String projectName, String projectDescription, UserEntity creator, String date,
                                      Category category, String website, int goal, int currentAmount, String... imagePaths) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH);

        Project project = new Project();
        project.setName(projectName);
        project.setDescription(projectDescription);
        project.setOwner(creator);
        project.setDate(LocalDate.parse(date, formatter));
        project.setCategory(category);
        project.setUrl(website);
        project.setGoal(goal);
        project.setCurrentAmount(currentAmount);



        List<Image> images = new ArrayList<>();
        for (String imagePath : imagePaths) {
            Image image = new Image(imagePath);
            images.add(image);
        }
        project.setImages(images);

        projectRepository.save(project);
        return project;
    }

    private UserEntity createAndSaveUser(String name,String email, String encodedPassword, String photo, String... roles) {
        Image profilePhoto;
        profilePhoto = new Image(photo);
        UserEntity user = new UserEntity(name,email, encodedPassword, profilePhoto, roles);
        userRepository.save(user);
        return user;
    }

    private Inversion createAndSaveInversion(UserEntity user, Project project, int amount, LocalDate date) {
        Inversion inversion = new Inversion(user, project, amount, date);
        Optional<UserEntity> userb = userRepository.findById(user.getId());
        Optional<Project> projectb = projectRepository.findById(project.getId());
        if(userb.isPresent() && projectb.isPresent()){
            userb.get().addInversion(inversion);
            projectb.get().addInversion(inversion);
            userRepository.save(userb.get());
            projectRepository.save(projectb.get());
            inversionRepository.save(inversion);
        }

        return inversion;
    }
}
