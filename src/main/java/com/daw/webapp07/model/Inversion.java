package com.daw.webapp07.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Inversion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    @JsonIgnore
    private Project project;
    private int amount;

    public Inversion() {
    }

    public Inversion(int amount){
        this.amount = amount;
    }

    public Inversion(UserEntity user, Project project, int amount) {
        this.user = user;
        this.project = project;
        this.amount = amount;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
