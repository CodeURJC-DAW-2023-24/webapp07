package com.daw.webapp07.DTO;

import com.daw.webapp07.model.*;


public class ProjectPreviewDTO {
    private Long id;

    private String name;

    private Category category;

    private String image;


    public ProjectPreviewDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.category = project.getCategory();
        this.image =  "https://localhost:8443/api/projects/" + this.id + "/images/" + project.getImages().get(0).getId();
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
