package com.daw.webapp07.controller;

import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.net.http.HttpRequest;
import java.util.Optional;

@ControllerAdvice
public class DefaultModelAtributes {
    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("user")
    public boolean addUserToModel(HttpServletRequest request){
        return request.isUserInRole("USER");
    }

    @ModelAttribute("userId")
    public Long addUserIdToModel(HttpServletRequest request){
        if(request.isUserInRole("USER")){
            Optional<UserEntity> user = userRepository.findByName(request.getUserPrincipal().getName());
            if(user.isPresent()){
                return user.get().getId();
            }
        }
        return null;
    }

}
