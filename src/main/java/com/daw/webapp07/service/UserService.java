package com.daw.webapp07.service;

import com.daw.webapp07.model.UserEntity;
import com.daw.webapp07.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> findUserByName(String name){
        return userRepository.findByName(name);
    }

    public Optional<UserEntity> findUserById(Long id){
        return userRepository.findById(id);
    }

    public void saveUser(UserEntity user){
        userRepository.save(user);
    }

    public List<UserEntity> findAll(){
        return userRepository.findAll();
    }



}
