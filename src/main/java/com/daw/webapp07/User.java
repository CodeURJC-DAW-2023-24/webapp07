package com.daw.webapp07;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private int HashedPassword;
    private ArrayList<Inversion> inversions;
    private ArrayList<Project> projects;

    public User(String name, String email, int HashedPassword) {
        this.name = name;
        this.email = email;
        this.HashedPassword = HashedPassword;
        this.inversions = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public User(){
        this.inversions = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void addInversion(Inversion i){
        inversions.add(i);
    }

    public void addProject(Project p){
        projects.add(p);
    }

    public ArrayList<Inversion> getInversions(){
        return inversions;
    }

    public ArrayList<Project> getProjects(){
        return projects;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getHashedPassword() {
        return HashedPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(int HashedPassword) {
        this.HashedPassword = HashedPassword;
    }


}
