package com.daw.webapp07.service;


import java.util.ArrayList;
import java.util.List;

import com.daw.webapp07.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daw.webapp07.model.UserEntity;


@Service
public class RepositoryUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return new org.springframework.security.core.userdetails.User(user.getName(),
                user.getEncodedPassword(), roles);

    }

    public boolean registerUser(UserEntity user) {
        if(userRepository.findByName(user.getName()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()){
            return false;
        }
        userRepository.save(user);
        emailService.sendEmail(user.getName(), user.getEmail(), "Welcome to SeedVentures");

        return true;
    }

}

