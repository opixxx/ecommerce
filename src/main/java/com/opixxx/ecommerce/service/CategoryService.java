package com.opixxx.ecommerce.service;

import java.util.List;

import com.opixxx.ecommerce.service.dto.CategoryDto;
import com.opixxx.ecommerce.service.dto.PaginationDto;

public interface CategoryService {
	List<CategoryDto.Category> getAllCategories(Integer level);

	// 특정 카테고리의 상품 목록 조회
	CategoryDto.CategoryProducts getCategoryProducts(
		Long categoryId,
		Boolean includeSubcategories,
		PaginationDto.PaginationRequest paginationRequest
	);

	// 특정 카테고리 조회 (하위 카테고리 포함)
	CategoryDto.Category getCategoryById(Long categoryId);
}
