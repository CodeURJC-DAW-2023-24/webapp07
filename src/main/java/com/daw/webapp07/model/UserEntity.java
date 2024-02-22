package com.daw.webapp07.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class UserEntity{

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Inversion> inversions;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Project> projects;

    @Lob
    @JsonIgnore
    private Blob profilePhoto;

    public UserEntity() {
    }

    public UserEntity(String name, String email, String encodedPassword, Blob photo, String... roles) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
        this.profilePhoto = photo;
        this.inversions = new ArrayList<>();
        this.projects = new ArrayList<>();
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

    public Blob getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Blob profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}

