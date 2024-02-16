package com.daw.webapp07;

import java.util.ArrayList;

public class Project {
    private String id;
    private String name;
    private String description;
    private String owner;
    private String date;
    private String category;
    private String url;
    private ArrayList<String> images;
    private int goal;
    private int currentAmount;
    private ArrayList<Inversion> inversions;

    public Project(String id, String name, String description, String owner, String startString, String category, String url, ArrayList<String> images, int goal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.date = startString;
        this.category = category;
        this.url = url;
        this.images = images;
        this.goal = goal;
        this.currentAmount = 0;
        this.inversions = new ArrayList<>();
    }

    public Project(String id, String name, String description, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.category = "";
        this.url = "";
        this.images = new ArrayList<>();
        this.goal = 206000;
        this.currentAmount = 1543;
        this.inversions = new ArrayList<>();
        this.date = "";
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
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

    public ArrayList<Inversion> getInversions() {
        return inversions;
    }

    public void addImage(String image) {
        this.images.add(image);
    }
}
