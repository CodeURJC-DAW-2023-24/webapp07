package com.daw.webapp07.repository;

import com.daw.webapp07.model.Inversion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InversionRepository extends JpaRepository<Inversion, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM inversion WHERE project_id = ?1", nativeQuery = true)
    void deleteByProjectId(long id);
}
