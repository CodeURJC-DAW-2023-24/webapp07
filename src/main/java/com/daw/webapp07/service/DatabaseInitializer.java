package com.daw.webapp07.service;


import com.daw.webapp07.model.Category;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import jakarta.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;


import java.util.List;

@Service
public class DatabaseInitializer {
    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    @Autowired
    private ProjectRepository projectRepository;

    @PostConstruct
    public void init() {

        createAndSaveProject(
                "KebabFinder",
                "Kebab Finder es una aplicación que te permite encontrar los mejores kebabs de tu ciudad. Podrás ver las opiniones de otros usuarios, ver la carta de los kebabs y hacer tus propias reseñas. Además, podrás solicitar un kebab a domicilio y pagar con tarjeta.",
                "MarkiIndustries",
                "28 January, 2024",
                Category.Technology,
                "https://kebabfinder.com",
                150000,
                0,
                FILES_FOLDER + "/kf/OIG2.jpg", FILES_FOLDER + "/kf/OIG4.jpg", FILES_FOLDER + "/kf/MENU-DONER-KEBAP-7.jpg"
        );

        createAndSaveProject(
                "EcoBike: La bicicleta eléctrica todo terreno",
                "Una bicicleta eléctrica diseñada para todo tipo de terrenos, desde montañas hasta calles urbanas. Equipada con baterías de larga duración y un motor potente, EcoBike busca revolucionar la movilidad sostenible.",
                "ecoworld",
                "2 February, 2024",
                Category.Entertainment,
                "https://ecobike.com",
                12000,
                0,
                FILES_FOLDER + "/ecobike/ecobike1.jpg", FILES_FOLDER + "/ecobike/ecobike2.jpg", FILES_FOLDER + "/ecobike/ecobike3.jpg"
        );

        createAndSaveProject(
                "CodeLearn",
                "Una plataforma educativa en línea que ofrece cursos interactivos para aprender programación, desde conceptos básicos hasta habilidades avanzadas en varios lenguajes de programación.",
                "proglearn",
                "16 January, 2024",
                Category.Education,
                "https://proglearn.com",
                12000,
                0,
                FILES_FOLDER + "/proglearn/proglearn1.jpeg", FILES_FOLDER + "/proglearn/proglearn3.jpg"
        );

        createAndSaveProject(
                "Trustphone",
                "El teléfono del que te puedes fiar, funcionamiento perfecto y fácilmente reparable",
                "Trustbusiness",
                "5 January, 2024",
                Category.Technology,
                "https://trustbusiness.com",
                50000,
                0,
                FILES_FOLDER + "/trustphone/trustphone1.jpg", FILES_FOLDER + "/trustphone/trustphone2.jpg", FILES_FOLDER + "/trustphone/trustphone3.jpg"
        );

        createAndSaveProject(
                "Purewater",
                "Un dispositivo compacto y portátil que utiliza tecnología de filtración avanzada para purificar el agua de fuentes no potables, haciéndola segura para beber en situaciones de emergencia o actividades al aire libre.",
                "Bewater",
                "5 February, 2024",
                Category.Health,
                "https://bewater.com",
                50000,
                0,
                FILES_FOLDER + "/trustphone/trustphone1.jpg", FILES_FOLDER + "/trustphone/trustphone2.jpg", FILES_FOLDER + "/trustphone/trustphone3.jpg"
        );

        createAndSaveProject(
                "Smart Garden",
                "Un sistema automatizado de jardinería que utiliza sensores para monitorear las condiciones del suelo y las plantas, ajustando automáticamente el riego y la iluminación para un crecimiento óptimo.",
                "MortezLab",
                "20 February, 2024",
                Category.Technology,
                "https://smartgarden.com",
                7000,
                0,
                FILES_FOLDER + "/smartgarden/smartgarden1.jpg", FILES_FOLDER + "/smartgarden/smartgarden2.jpg", FILES_FOLDER + "/smartgarden/smartgarden3.jpg"
        );



/**
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
                "/img/proglearn/proglearn1.jpeg", "/img/proglearn/proglearn2.jpg", "/img/proglearn/proglearn3.jpg",
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



    **/
    }

    private void createAndSaveProject(String projectName, String projectDescription, String creator, String date,
                                      Category category, String website, int goal, int currentAmount, String... imagePaths) {
        Project project = new Project();
        project.setName(projectName);
        project.setDescription(projectDescription);
        project.setOwner(creator);
        project.setDate(date);
        project.setCategory(category);
        project.setUrl(website);
        project.setGoal(goal);
        project.setCurrentAmount(currentAmount);



        List<Blob> images = new ArrayList<>();
        try {
            for (String imagePath : imagePaths) {
                Blob imageBlob = createBlob(imagePath);
                images.add(imageBlob);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        project.setImages(images);

        projectRepository.save(project);
    }

    private Blob createBlob(String imagePath) throws IOException, SQLException {
        try (FileInputStream fileInputStream = new FileInputStream(imagePath)) {
            byte[] data = fileInputStream.readAllBytes();
            return new javax.sql.rowset.serial.SerialBlob(data);
        }
    }

}
