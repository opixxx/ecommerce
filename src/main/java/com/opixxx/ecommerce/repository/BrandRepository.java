package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}

