package com.daw.webapp07.model;

import jakarta.persistence.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private UserEntity owner;
    private LocalDate date;
    private Category category;
    private String url;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    private int goal;
    private int currentAmount;

    @OneToMany(mappedBy = "project" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Inversion> inversions;

    public Project() {
        this.comments = new ArrayList<>();
        this.inversions = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public Project( String name, String description, UserEntity owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.inversions = new ArrayList<>();
        this.category= Category.Other;
        this.date = null;
        this.url = "";
        this.images = new ArrayList<>();
        this.goal = 0;
        this.currentAmount = 0;
        this.comments = new ArrayList<>();

    }

    public Project( String name, String description, UserEntity owner, String date, Category category, String url, ArrayList<Image> images, int goal, int currentAmount, ArrayList<Inversion> inversions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH);

        this.name = name;
        this.description = description;
        this.owner = owner;
        this.date = LocalDate.parse(date, formatter);
        this.category = category;
        this.url = url;
        this.images = images;
        this.goal = goal;
        this.currentAmount = currentAmount;
        this.inversions = inversions;
        this.comments = new ArrayList<>();
    }

    public Project( String name, String description, UserEntity owner, LocalDate date, Category category, String url, ArrayList<Image> images, int goal, int currentAmount, ArrayList<Inversion> inversions) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.date = date;
        this.category = category;
        this.url = url;
        this.images = images;
        this.goal = goal;
        this.currentAmount = currentAmount;
        this.inversions = inversions;
        this.comments = new ArrayList<>();
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

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void addImage(Image im){
        this.images.add(im);
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public void addInversion(Inversion inversion) {
        this.inversions.add(inversion);
        this.currentAmount += inversion.getAmount();
    }

    public List<Inversion> getInversions() {
        return inversions;
    }

}
