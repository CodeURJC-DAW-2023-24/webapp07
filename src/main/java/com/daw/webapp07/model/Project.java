package com.daw.webapp07.model;

import jakarta.persistence.*;

import java.util.ArrayList;
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

    private String image1;
    private String image2;
    private String image3;

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
        this.image1 = "";
        this.image2 = "";
        this.image3 = "";
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
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
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
