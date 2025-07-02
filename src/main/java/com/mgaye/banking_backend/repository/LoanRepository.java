package com.mgaye.banking_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.Loan;

// LoanRepository.java
public interface LoanRepository extends JpaRepository<Loan, String> {

        @Query("""
                        SELECT l FROM Loan l
                        WHERE l.userId = :userId
                        AND l.status IN ('ACTIVE', 'DEFAULTED')
                        ORDER BY l.dueDate ASC
                        """)
        List<Loan> findActiveLoans(@Param("userId") String userId);

        @Query(value = """
                        SELECT l FROM Loan l
                        WHERE l.dueDate BETWEEN :start AND :end
                        AND l.status = 'ACTIVE'
                        """, nativeQuery = false)
        List<Loan> findLoansDueBetween(@Param("start") LocalDate start,
                        @Param("end") LocalDate end);

        @Modifying
        @Query("UPDATE Loan l SET l.status = 'DEFAULTED' " +
                        "WHERE l.status = 'ACTIVE' AND l.dueDate < CURRENT_DATE")
        void markDefaultedLoans();

        Optional<Loan> findById(UUID loanId);
}