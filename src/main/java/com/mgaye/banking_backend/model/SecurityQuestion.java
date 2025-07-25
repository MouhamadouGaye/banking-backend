package com.mgaye.banking_backend.model;

import java.util.UUID;

import org.springframework.security.access.method.P;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// SecurityQuestion.java
@Entity
@Table(name = "security_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;
}
