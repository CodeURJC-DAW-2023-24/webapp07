package com.daw.webapp07.service;


import com.daw.webapp07.model.*;
import com.daw.webapp07.repository.CommentRepository;
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
    // we take the values from the application.properties file
    @Value("${security.user}")
    private String admin;

    // we take the values from the application.properties file
    @Value("${security.encodedPassword}")
    private String adminpass;
    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InversionRepository inversionRepository;

    @Autowired
    private CommentRepository commentRepository;

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
                "Kebab Finder is an application that allows you to find the best kebab places in your city. You can view reviews from other users, check out the menu of kebab restaurants, and write your own reviews. Additionally, you can order a kebab for delivery and pay with a card.",
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
                "EcoBike",
                "An electric bicycle designed for all types of terrain, from mountains to urban streets. Equipped with long-lasting batteries and a powerful motor, EcoBike aims to revolutionize sustainable mobility.",
                ecoworld,
                "28 January, 2022",
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
                "An online educational platform offering interactive courses to learn programming, from basic concepts to advanced skills in various programming languages.",
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
                "The trustworthy phone: flawless performance and easily repairable.",
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
                "A compact and portable device that uses advanced filtration technology to purify water from non-potable sources, making it safe for drinking in emergency situations or outdoor activities.",
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
                "An automated gardening system that utilizes sensors to monitor soil conditions and plants, automatically adjusting watering and lighting for optimal growth.",
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
                "A 2D adventure game with pixelated graphics. Explore fascinating worlds, solve puzzles, and face exciting challenges in this indie game developed by a small passionate team.",
                PixelStudios,
                "5 August, 2023",
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
                "An online platform that offers resources and support for mental health. It provides access to therapists, meditation tools, and support communities to enhance emotional well-being.",
                MindCare,
                "12 July, 2023",
                Category.Health,
                "https://mindwell.com",
                25000,
                0,
                FILES_FOLDER + "/mindwell/mindwell1.jpeg", FILES_FOLDER + "/mindwell/mindwell2.png"
        );

        UserEntity LingoTech = createAndSaveUser("LingoTech","Lingotech@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/lingolearn/lingolearn1.jpeg","USER");
        Project LingoLearn = createAndSaveProject(
                "LingoLearn",
                "An interactive platform for effective language learning. It offers personalized courses, conversation practice, and progress tracking to help users achieve fluency.",
                LingoTech,
                "22 September, 2023",
                Category.Education,
                "https://lingolearn.com",
                15000,
                0,
                FILES_FOLDER + "/lingolearn/lingolearn1.jpeg", FILES_FOLDER + "/lingolearn/lingolearn2.jpeg", FILES_FOLDER + "/lingolearn/lingolearn3.png"
        );

        UserEntity GreenHarbor = createAndSaveUser("GreenHarbor","greenharbor@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/urbanharvest/urbanharvest1.jpeg","USER");
        Project UrbanHarvest = createAndSaveProject(
                "UrbanHarvest",
                "An urban farming system that enables communities to grow fresh food locally. It utilizes sustainable technologies to maximize production in urban environments.",
                GreenHarbor,
                "10 October, 2023",
                Category.Sustainability,
                "https://urbanharvest.com",
                22000,
                0,
                FILES_FOLDER + "/urbanharvest/urbanharvest1.jpeg", FILES_FOLDER + "/urbanharvest/urbanharvest2.jpeg", FILES_FOLDER + "/urbanharvest/urbanharvest3.jpeg"
        );

        UserEntity CosmicTech = createAndSaveUser("CosmicTech","cosmictech@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/galaxygetaway/galaxygetaway1.jpeg","USER");
        Project GalaxyGetaway = createAndSaveProject(
                "GalaxyGetaway",
                "Explore outer space with this app that offers virtual space travel. Discover distant planets, participate in simulated space missions, and learn about the universe.",
                CosmicTech,
                "15 November, 2023",
                Category.Technology,
                "https://galaxygetaway.com",
                30000,
                0,
                FILES_FOLDER + "/galaxygetaway/galaxygetaway1.jpeg", FILES_FOLDER + "/galaxygetaway/galaxygetaway2.jpeg"
        );

        UserEntity LiveMusicEnt = createAndSaveUser("LiveMusicEnt","LiveMusicEnt@gmail.com","$2a$12$bVuq2TEUH/cNkJhyct.ob.wXkOA08wR67ZfLuaKy6tKnzMtdPhbV.",FILES_FOLDER + "/livebeat/liveBeat1.jpeg","USER");
        Project LiveBeat = createAndSaveProject(
                "LiveBeat",
                "Enjoy live concerts from the comfort of your home. LiveBeat provides access to exclusive performances by popular and emerging artists, all in high definition and surround sound.",
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
                "Promoting healthy eating habits with nutritious recipes and personalized meal plans. NutriFuel helps individuals achieve their wellness goals through balanced nutrition.",
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
                "30 November, 2023",
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

        BerniBear.addComment(new Comment(u2, BerniBear, "I love the animation style! Can't wait to see more."));
        BerniBear.addComment(new Comment(u3, BerniBear, "I've been a fan since the first season. Looking forward to the next one!"));
        BerniBear.addComment(new Comment(u4, BerniBear, "I'm happy to support this project. Keep up the good work!"));
        projectRepository.save(BerniBear);

        KebabFinder.addComment(new Comment(u1, KebabFinder, "I'm excited to see this project come to life!"));
        KebabFinder.addComment(new Comment(u2, KebabFinder, "I love kebabs! Can't wait to try this app."));
        KebabFinder.addComment(new Comment(u3, KebabFinder, "I've been waiting for an app like this!"));
        KebabFinder.addComment(new Comment(u4, KebabFinder, "I'm happy to support this project. Keep up the good work!"));
        projectRepository.save(KebabFinder);

        GalaxyGetaway.addComment(new Comment(u1, GalaxyGetaway, "I love space exploration! Looking forward to trying this app."));
        GalaxyGetaway.addComment(new Comment(u2, GalaxyGetaway, "This looks like a fun way to learn about the universe!"));
        GalaxyGetaway.addComment(new Comment(u3, GalaxyGetaway, "I'm excited to see what this app has to offer."));
        GalaxyGetaway.addComment(new Comment(u4, GalaxyGetaway, "I'm happy to support this project. Keep up the good work!"));
        projectRepository.save(GalaxyGetaway);

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
