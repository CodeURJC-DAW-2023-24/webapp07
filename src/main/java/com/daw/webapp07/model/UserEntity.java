package com.daw.webapp07.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class UserEntity{
    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String name;

    private String encodedPassword;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Inversion> inversions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project> projects;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image profilePhoto;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments;

    public UserEntity() {
        this.inversions = new ArrayList<>();
        this.profilePhoto = new Image(FILES_FOLDER + "/profiles/user1.png");
    }

    public UserEntity(String name, String email, String encodedPassword, Image photo, String... roles) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
        this.profilePhoto = photo;
        this.inversions = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Inversion> getInversions() {
        return inversions;
    }

    public void setInversions(ArrayList<Inversion> inversions) {
        this.inversions = inversions;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean hasInversions() {
        return !inversions.isEmpty();
    }

    public String toString(){
        return this.name;
    }

    public Image getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Image profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        if(this.comments == null){
            this.comments = new ArrayList<>();
        }

        this.comments.add(comment);
    }

    public void addInversion(Inversion inversion){
        if(this.inversions == null){
            this.inversions = new ArrayList<>();
        }
        this.inversions.add(inversion);
    }

    public int getTotalInvested() {
        int total = 0;

        for (int i = 0; i < inversions.size(); i++) {
            total += inversions.get(i).getAmount();
        }
        return total;
    }
}

