package com.daw.webapp07.controller;

import com.daw.webapp07.model.*;
import com.daw.webapp07.repository.*;
import com.daw.webapp07.service.DatabaseInitializer;
import com.daw.webapp07.service.ProjectService;
import com.daw.webapp07.service.PdfService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.w3c.dom.events.Event;


import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    InversionRepository inversionRepository;

    @Autowired
    ProjectService projectService;

    @GetMapping("/")
    public String innerPage(Model model, HttpServletRequest request) {
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
            if(user.isPresent() && user.get().hasInversions()){
                System.out.println("Recomendando");
                model.addAttribute("projects", projectService.searchRecommendedProjects(0,12,user.get().getId()));
                model.addAttribute("user", user);

            }

        }else
        {
            model.addAttribute("projects", projectService.searchProjects(0, 12));
        }


        return "inner-page";
    }


    @GetMapping("/projects")
    public String getProjects(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "6") int size ) {

        model.addAttribute("projects", projectService.searchProjects(page, size));

        return "portfolio";
    }


    @GetMapping("/recomendedprojects")
    public String getRecomendedProjects(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "6") int size ) {

        model.addAttribute("projects", projectService.searchProjects(page, size));

        return "portfolio";
    }


    @GetMapping("/project-details/{id}/")
    public String home(Model model, @PathVariable Long id, HttpServletRequest request) {
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();

        if(request.isUserInRole("USER")){
            if  (request.getUserPrincipal().getName().equals(project.getOwner().getName()) || request.isUserInRole("ADMIN")){
                model.addAttribute("privileged",true);
            }
        }



        model.addAttribute("project", project);
        model.addAttribute("id", id);


        HashMap<String,Integer> donors = new HashMap<>();
        int total = 0;
        for(Inversion i: project.getInversions()){
            total+=i.getAmount();
            if(donors.containsKey(i.getUser().getName())){
                donors.put(i.getUser().getName(),donors.get(i.getUser().getName())+i.getAmount());
            }else{
                donors.put(i.getUser().getName(),i.getAmount());
            }
        }

        List<String> names = new ArrayList<>(donors.keySet());
        List<Integer> quantities = new ArrayList<>(donors.values());
        names.sort((a,b)->donors.get(b).compareTo(donors.get(a)));
        quantities.sort((a,b)->b.compareTo(a));

        List<String> times = new ArrayList<>();
        List<Integer> pastmoney = new ArrayList<>();
        Calendar timeNow = Calendar.getInstance();
        Calendar oldest = Calendar.getInstance();
        oldest.set(project.getDate().getYear(), project.getDate().getMonthValue() -1, project.getDate().getDayOfMonth());
        oldest.add(Calendar.YEAR,1);
        boolean moreThanAYear = oldest.before(timeNow);
        oldest.add(Calendar.YEAR, -1);
        HashMap<String, Integer> where = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        List<String> months = new ArrayList<>(Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"));
        int i = 0;
        while (oldest.before(timeNow)) {
            System.out.println("Mesasd: "+oldest.get(Calendar.YEAR));
            if(!moreThanAYear) {
                times.add(months.get(oldest.get(Calendar.MONTH)));
            }
            else {
                times.add(months.get(oldest.get(Calendar.MONTH)) + " " + oldest.get(Calendar.YEAR));
            }
            System.out.println(times);
            where.put(sdf.format(oldest.getTime()),i);
            oldest.add(Calendar.MONTH, 1);
            i++;
        }
        for(int j = 0; j < times.size(); j++){
            pastmoney.add(0);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy", Locale.ENGLISH);
        for(Inversion i2: project.getInversions()){
            int index = where.get(i2.getDate().format(dtf));
            pastmoney.set(index,pastmoney.get(index)+i2.getAmount());
        }
        System.out.println(pastmoney);
        for(int k = 1; k < pastmoney.size(); k++){
            pastmoney.set(k,pastmoney.get(k)+pastmoney.get(k-1));
            System.out.println(pastmoney);
        }

        model.addAttribute("donors",array_to_string_jsarray(names));
        model.addAttribute("quantities", array_to_int_jsarray(quantities));
        model.addAttribute("times",array_to_string_jsarray(times));
        model.addAttribute("pastmoney", array_to_int_jsarray(pastmoney));
        model.addAttribute("hasInversions", !project.getInversions().isEmpty());

        return "project-details";
    }

    @GetMapping("/projects/{id}/images/{index}")
    public ResponseEntity<Object> displayImage(@PathVariable Long id, @PathVariable int index) throws SQLException{
        Project project = projectRepository.findById(id).orElseThrow();
        index--; //index - 1 porque mustache empieza a contar desde 1
        List<Image> images = project.getImages();
        if (index < images.size()){
            Resource file = new InputStreamResource(images.get(index).getImageFile().getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(images.get(index).getImageFile().length())
                    .body(file);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/users/{id}/profile")
    public ResponseEntity<Object> displayProfilePhoto(@PathVariable Long id) throws SQLException{
        UserEntity userEntity = userRepository.findById(id).orElseThrow();
        Resource file = new InputStreamResource(userEntity.getProfilePhoto().getImageFile().getBinaryStream());

            return ResponseEntity.ok()
                    .contentLength(userEntity.getProfilePhoto().getImageFile().length())
                    .body(file);

    }


    @PostMapping("/createProject")
    public String createProject(Project project,
                                @RequestParam("file") MultipartFile[] files,
                                HttpServletRequest request) {

        Optional<UserEntity> checkQuery = userRepository.findByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity query = checkQuery.get();
        project.setOwner(query);
        LocalDate date = LocalDate.now();
        project.setDate(date);

        for (MultipartFile file : files) {
            Image image = new Image(file);
            project.addImage(image);
        }

        projectRepository.save(project);
        return "redirect:/project-details/" + project.getId() + "/";
    }



    @PostMapping("/project-details/{id}/comment")
    String comment(@PathVariable Long id, Comment comment, HttpServletRequest request, Model model){

        Comment newComment = new Comment(comment.getText());
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();

        newComment.setProject(project);
        Optional<UserEntity> checkQuery = userRepository.findByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity query = checkQuery.get();
        newComment.setUser(query);
        project.addComment(newComment);
        projectRepository.save(project);

        return "redirect:/project-details/" + id + "/";
    }

    @PostMapping("/project-details/{id}/donate")
    String donate(@PathVariable Long id, int donation, HttpServletRequest request, Model model){

        Inversion newInversion = new Inversion(donation);
        LocalDate date = LocalDate.now();
        newInversion.setDate(date);
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();
        newInversion.setProject(project);
        Optional<UserEntity> checkQuery = userRepository.findByName(request.getUserPrincipal().getName());
        if (checkQuery == null)
            return "redirect:/error-page";
        UserEntity user = checkQuery.get();
        newInversion.setUser(user);
        project.addInversion(newInversion);
        projectRepository.save(project);
        user.addInversion(newInversion);
        userRepository.save(user);


        return "redirect:/project-details/" + id + "/";
    }

    @GetMapping ("/project-details/{id}/delete")
    String deleteProject(@PathVariable Long id, HttpServletRequest request){
        Optional<Project> checkProject = projectRepository.findById(id);
        if (checkProject == null)
            return "redirect:/error-page";
        Project project = checkProject.get();
        if (!project.getInversions().isEmpty())
            return "redirect:/error-page";

        if (request.isUserInRole("ADMIN") || request.getUserPrincipal().getName().equals(project.getOwner().getName())){
            projectRepository.deleteById(id);
        }

        return "redirect:/";
        }


    @GetMapping("/editProject/{id}")
    public String editProject(Model model, @PathVariable long id, HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName();
        Optional<Project> project = projectRepository.findById(id);
        Optional<UserEntity> user = userRepository.findByName(userName);
        if(user.isPresent() && project.isPresent() && (project.get().getOwner().equals(user.get()) || request.isUserInRole("ADMIN"))){
            model.addAttribute("isEditing", true);
            model.addAttribute("project", project.get());
            model.addAttribute("categories", Category.values());
            return "create-project";
        }
        return "error-page";

    }


    @PostMapping("/editProject/{id}")
    public String replaceProject(@PathVariable long id, Project newProject,
                                 @RequestParam("file") MultipartFile[] files) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            Project proj = project.get();
            if(!files[0].isEmpty() ){
                for (MultipartFile file : files) {
                    Image image = new Image(file);
                    proj.addImage(image);

                }
            }
            proj.setName(newProject.getName());
            proj.setDescription(newProject.getDescription());
            proj.setCategory(newProject.getCategory());
            proj.setUrl(newProject.getUrl());
            proj.setGoal(newProject.getGoal());

            projectRepository.save(proj);
            }

        return "redirect:/project-details/" + id + "/";
    }

    @Controller
    public class PdfController {

        @Autowired
        private PdfService pdfGenerationService;

        @Autowired
        private ProjectRepository projectRepository;

        @GetMapping("/project-details/{id}/generate-pdf")
        public void generatePdf(@PathVariable long id, HttpServletResponse response) throws IOException {
            Optional<Project> project = projectRepository.findById(id);
            if (project.isPresent()) {
                pdfGenerationService.generatePdf(project.get(), response, id);
            }
        }
    }


    private List<Pair<Float,UserEntity>> getSimilarUsers(UserEntity user, HashMap<UserEntity,HashMap<Category,Float>> percentages){
        HashMap<Category, Float> base = percentages.get(user);
        List<Pair<Float,UserEntity>> similar = new ArrayList<>();
        for(UserEntity u: percentages.keySet()){
            if(u.equals(user)){
                continue;
            }
            float points = 0;
            for(Category c: Category.values()){
                points+=Math.abs(base.get(c)-percentages.get(u).get(c));
            }
            similar.add(new Pair<>(points,u));
        }
        similar.sort((a,b)->a.a.compareTo(b.a));
        return similar;
    }

    private HashMap<UserEntity,HashMap<Category,Float>> getPercentages(){
        HashMap<UserEntity,HashMap<Category,Float>> ups = new HashMap<>();
        for(UserEntity u: userRepository.findAll()) {
            HashMap<Category,Float> up = new HashMap<>();
            float total = 0;
            for(Category c: Category.values()){
                up.put(c,0f);
            }
            for(Inversion i: u.getInversions()){
                up.put(i.getProject().getCategory(),up.get(i.getProject().getCategory())+i.getAmount());
                total+=i.getAmount();
            }
            for(Category c: Category.values()){
                up.put(c,up.get(c)/total);
            }
                ups.put(u,up);
        }
        return ups;
    }

    private List<Project> recommendationSimple(UserEntity user){
        HashMap<UserEntity,HashMap<Category,Float>> percentages = getPercentages();
        List<Pair<Float, UserEntity>> users = getSimilarUsers(user, percentages);
        List<Project> projects = new ArrayList<>();
        HashSet<Project> set = new HashSet<>();
        for(Inversion i: user.getInversions()){
            set.add(i.getProject());
        }
        for(Pair<Float,UserEntity> p: users){
            for(Inversion i: p.b.getInversions()){
                if(!set.contains(i.getProject())){
                    projects.add(i.getProject());
                    set.add(i.getProject());
                }
            }
        }
        return projects;
    }

    private List<Project> likelihoodOfDonation(UserEntity user){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private String array_to_string_jsarray(List<String> list){
        String ar = "[";
        for(String s: list){
            ar += "'"+s+"',";
        }
        if (ar.length() > 1) {ar = ar.substring(0,ar.length()-1);}
        ar += "]";
        System.out.println(ar);
        return ar;
    }

    private String array_to_int_jsarray(List<Integer> list){
        String ar = "[";
        for(Integer i: list){
            ar += i+",";
        }
        if (ar.length() > 1) {ar = ar.substring(0,ar.length()-1);}
        ar += "]";
        System.out.println(ar);
        return ar;
    }

}
