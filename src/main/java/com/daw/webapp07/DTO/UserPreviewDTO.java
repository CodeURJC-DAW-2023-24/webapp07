package com.daw.webapp07.DTO;

import com.daw.webapp07.model.UserEntity;

public class UserPreviewDTO {

    private String username;

    private String profileImage;

    public UserPreviewDTO(UserEntity user) {
        this.profileImage = "https://localhost:8443/api/users/" + user.getId() + "/profile";
        this.username = user.getName();

    }

    public UserPreviewDTO() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
