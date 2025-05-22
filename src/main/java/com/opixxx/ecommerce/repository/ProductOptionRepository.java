package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.ProductOption;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}