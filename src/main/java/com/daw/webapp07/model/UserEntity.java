package com.daw.webapp07.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String encodedPassword;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @OneToMany(cascade = CascadeType.ALL)
    private ArrayList<Inversion> inversions;

    @OneToMany(cascade = CascadeType.ALL)
    private ArrayList<Project> projects;

    public UserEntity() {
    }

    public UserEntity(String name, String encodedPassword, String... roles) {
        this.name = name;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
        this.inversions = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Inversion> getInversions() {
        return inversions;
    }

    public void setInversions(ArrayList<Inversion> inversions) {
        this.inversions = inversions;
    }

    public ArrayList<Project> getProjects() {
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
}

