package com.daw.webapp07;

public class Inversion {
    private User user;
    private Project project;
    private int amount;

    public Inversion(User user, Project project, int amount) {
        this.user = user;
        this.project = project;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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