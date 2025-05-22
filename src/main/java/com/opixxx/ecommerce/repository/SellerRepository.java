package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}