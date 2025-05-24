package com.opixxx.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsBySlug(String slug);

	@EntityGraph(attributePaths = {"price", "brand", "seller"})
	Page<Product> findAll(Specification<Product> spec, Pageable pageable);

	Page<Product> findByCategoriesIdIn(List<Long> categoryIds, Pageable pageable);

	Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);

}