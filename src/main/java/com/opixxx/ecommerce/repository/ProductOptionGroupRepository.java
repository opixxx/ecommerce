package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.ProductOptionGroup;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long> {
}