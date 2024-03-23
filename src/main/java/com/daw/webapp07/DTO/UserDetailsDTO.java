package com.daw.webapp07.DTO;

import com.daw.webapp07.model.UserEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

import java.util.List;

public class UserDetailsDTO {
    private Long id;
    private String name;

    private String email;

    private List<String> roles;

    private String profileImage;

    public UserDetailsDTO(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.profileImage = "https://localhost:8443/api/users/" + user.getId() + "/profile";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
