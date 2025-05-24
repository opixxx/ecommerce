package com.opixxx.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findByLevel(Integer level);
}