package com.mgaye.banking_backend.repository;

import java.util.List;

import javax.smartcardio.Card;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// CardRepository.java
public interface CardRepository extends JpaRepository<Card, String> {

    @EntityGraph(attributePaths = { "linkedAccount" })
    List<Card> findByUserIdAndStatus(String userId, CardStatus status);

    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.expirationDate > CURRENT_DATE")
    List<Card> findActiveCardsByUser(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE Card c SET c.status = 'EXPIRED' WHERE c.expirationDate < CURRENT_DATE")
    void expireOldCards();
}