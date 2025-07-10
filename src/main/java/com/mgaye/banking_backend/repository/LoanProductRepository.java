
package com.mgaye.banking_backend.repository; // Adjust package

import com.mgaye.banking_backend.model.LoanProduct;
import com.mgaye.banking_backend.model.Loan.LoanType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Long> {
    List<LoanProduct> findByType(LoanType type); // For filtering by loan type
}