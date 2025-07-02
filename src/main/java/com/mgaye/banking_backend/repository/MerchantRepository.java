package com.mgaye.banking_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgaye.banking_backend.model.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, String> {

}
