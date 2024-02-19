package com.daw.webapp07.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.webapp07.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

}
