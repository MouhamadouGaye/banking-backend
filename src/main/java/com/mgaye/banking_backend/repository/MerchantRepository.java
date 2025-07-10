package com.mgaye.banking_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {

}
