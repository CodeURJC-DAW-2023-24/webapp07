package com.daw.webapp07.DTO;

import com.daw.webapp07.model.Inversion;

import java.time.LocalDate;


public class InversionDTO {
    private Long userId;
    private Long projectId;
    private int amount;
    private LocalDate date;

    public InversionDTO(Inversion inversion) {
        this.userId = inversion.getUser().getId();
        this.projectId = inversion.getProject().getId();
        this.amount = inversion.getAmount();
        this.date = inversion.getDate();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
