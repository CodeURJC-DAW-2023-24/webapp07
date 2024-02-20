package com.daw.webapp07.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String owner;
    private String date;
    private String category;
    private String url;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;

    private int goal;
    private int currentAmount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Inversion> inversions;

    public Project() {
    }

    public Project( String name, String description, String owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.inversions = new ArrayList<>();
        this.category= "";
        this.date = "";
        this.url = "";
        this.images = new ArrayList<>();
        this.goal = 0;
        this.currentAmount = 0;
    }

    public Project( String name, String description, String owner, String date, String category, String url, String image1, String image2, String image3, int goal, int currentAmount, ArrayList<Inversion> inversions) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.date = date;
        this.category = category;
        this.url = url;
        this.images = new ArrayList<>(Arrays.asList(image1, image2, image3));
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void addImage(String im){
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addInversion(Inversion inversion) {
        this.inversions.add(inversion);
        this.currentAmount += inversion.getAmount();
    }

    public List<Inversion> getInversions() {
        return inversions;
    }

}
