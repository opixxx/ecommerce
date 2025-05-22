package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}