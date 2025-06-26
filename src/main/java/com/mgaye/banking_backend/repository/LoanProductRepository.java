
package com.mgaye.banking_backend.repository; // Adjust package

import com.mgaye.banking_backend.model.LoanProduct;
import com.mgaye.banking_backend.model.Loan.LoanType;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanProductRepository extends JpaRepository<LoanProduct, Long> {
    List<LoanProduct> findByType(LoanType type); // For filtering by loan type
}