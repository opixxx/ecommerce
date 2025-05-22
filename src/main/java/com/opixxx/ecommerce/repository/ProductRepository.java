package com.opixxx.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsBySlug(String slug);

	Page<Product> findAll(Specification<Product> spec, Pageable pageable);

}