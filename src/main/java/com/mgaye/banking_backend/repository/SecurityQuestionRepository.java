package com.mgaye.banking_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.SecurityQuestion;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, String> {

    // Correct method signature for deleting by userId
    @Modifying
    @Query("DELETE FROM SecurityQuestion s WHERE s.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    // Optional: Find questions by userId
    List<SecurityQuestion> findByUserId(String userId);

}
