package com.mgaye.banking_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.dto.response.CardResponse;
import com.mgaye.banking_backend.model.Card;
import com.mgaye.banking_backend.model.enums.CardStatus;

// CardRepository.java
@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    List<Card> findByUserId(String userId);

    Optional<Card> findById(String cardId);

    Boolean existsByIdAndUserId(String cardId, String userId);

    @EntityGraph(attributePaths = { "linkedAccount" })
    List<Card> findByUserIdAndStatus(String userId, CardStatus status);

    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.expirationDate > CURRENT_DATE")
    List<Card> findActiveCardsByUser(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE Card c SET c.status = 'EXPIRED' WHERE c.expirationDate < CURRENT_DATE")
    void expireOldCards();
}