package com.daw.webapp07.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.webapp07.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

}
