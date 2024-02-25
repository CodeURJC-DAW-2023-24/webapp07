package com.daw.webapp07.repository;

import com.daw.webapp07.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerName(String ownerName);
}
