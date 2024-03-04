package com.daw.webapp07.repository;


import com.daw.webapp07.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}