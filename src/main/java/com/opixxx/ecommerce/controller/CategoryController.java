package com.opixxx.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opixxx.ecommerce.service.CategoryService;
import com.opixxx.ecommerce.service.dto.CategoryDto;
import com.opixxx.ecommerce.service.dto.PaginationDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryDto.Category>>> getAllCategories(
		@RequestParam(required = false) Integer level
	) {
		return ResponseEntity.ok(
			ApiResponse.success(
				categoryService.getAllCategories(level),
				"카테고리 목록을 성공적으로 조회했습니다."
			)
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryDto.Category>> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(
			ApiResponse.success(
				categoryService.getCategoryById(id),
				"카테고리 정보를 성공적으로 조회했습니다."
			)
		);
	}

	@GetMapping("/{id}/products")
	public ResponseEntity<ApiResponse<CategoryDto.CategoryProducts>> getCategoryProducts(
		@PathVariable Long id,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int perPage,
		@RequestParam(defaultValue = "created_at:desc") String sort,
		@RequestParam(defaultValue = "true") Boolean includeSubcategories) {

		var paginationRequest = PaginationDto.PaginationRequest.builder()
			.page(page)
			.size(perPage)
			.sort(sort)
			.build();

		return ResponseEntity.ok(
			ApiResponse.success(
				categoryService.getCategoryProducts(id, includeSubcategories, paginationRequest),
				"카테고리 상품 목록을 성공적으로 조회했습니다."
			)
		);
	}

}
