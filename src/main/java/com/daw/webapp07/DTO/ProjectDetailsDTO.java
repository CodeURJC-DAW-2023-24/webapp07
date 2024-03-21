package com.daw.webapp07.DTO;

import com.daw.webapp07.model.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class ProjectDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private String owner;
    private LocalDate date;
    private Category category;
    private String url;
    private String images;
    private int goal;
    private int currentAmount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public ProjectDetailsDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.owner = project.getOwner().getName();
        this.date = project.getDate();
        this.category = project.getCategory();
        this.url = project.getUrl();
        this.images =  "http://localhost:8443/api/proyects/" + this.id + "/images";
        this.goal = project.getGoal();
        this.currentAmount = project.getCurrentAmount();
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
