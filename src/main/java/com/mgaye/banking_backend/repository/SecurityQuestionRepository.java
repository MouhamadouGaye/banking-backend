package com.mgaye.banking_backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.SecurityQuestion;

@Repository
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, String> {

    // Correct method signature for deleting by userId
    @Modifying
    @Query("DELETE FROM SecurityQuestion s WHERE s.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    // Optional: Find questions by userId
    List<SecurityQuestion> findByUserId(UUID userId);

}
