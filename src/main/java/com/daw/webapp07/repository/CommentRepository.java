package com.daw.webapp07.repository;

import com.daw.webapp07.model.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment WHERE project_id = ?1", nativeQuery = true)
    void deleteByProjectId(long id);
}
