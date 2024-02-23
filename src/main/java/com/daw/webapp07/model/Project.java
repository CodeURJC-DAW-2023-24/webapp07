package com.daw.webapp07.model;

import jakarta.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private UserEntity owner;
    private String date;
    private Category category;
    private String url;

    /*@Lob
    @JsonIgnore
    private List<Blob> images;*/

    private int goal;
    private int currentAmount;

    @OneToMany
    private List<Comment> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inversion> inversions;

    public Project() {
    }

    public Project( String name, String description, UserEntity owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.inversions = new ArrayList<>();
        this.category= Category.Other;
        this.date = "";
        this.url = "";
        //this.images = new ArrayList<>();
        this.goal = 0;
        this.currentAmount = 0;
        this.comments = new ArrayList<>();

    }

    public Project( String name, String description, UserEntity owner, String date, Category category, String url, ArrayList<Blob> images, int goal, int currentAmount, ArrayList<Inversion> inversions) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.date = date;
        this.category = category;
        this.url = url;
        //this.images = new ArrayList<>();//images;
        this.goal = goal;
        this.currentAmount = currentAmount;
        this.inversions = inversions;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
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

    public List<Blob> getImages() {
        return null;
    }

    public void setImages(List<Blob> images) {
        //this.images = images;
    }

    public void addImage(Blob im){
        //this.images.add(im);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addInversion(Inversion inversion) {
        this.inversions.add(inversion);
        this.currentAmount += inversion.getAmount();
    }

    public List<Inversion> getInversions() {
        return inversions;
    }

}
