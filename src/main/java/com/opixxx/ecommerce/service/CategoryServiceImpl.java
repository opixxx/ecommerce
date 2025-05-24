package com.opixxx.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opixxx.ecommerce.domain.Category;
import com.opixxx.ecommerce.domain.Product;
import com.opixxx.ecommerce.exception.NotFoundException;
import com.opixxx.ecommerce.repository.CategoryRepository;
import com.opixxx.ecommerce.repository.ProductRepository;
import com.opixxx.ecommerce.service.dto.CategoryDto;
import com.opixxx.ecommerce.service.dto.PaginationDto;
import com.opixxx.ecommerce.service.mapper.CategoryMapper;
import com.opixxx.ecommerce.service.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final CategoryMapper categoryMapper;
	private final ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDto.Category> getAllCategories(Integer level) {
		if (level != null) {
			List<Category> categories = categoryRepository.findByLevel(level);

			List<Category> allCategories = categoryRepository.findAll();

			Map<Long, List<Category>> childrenMap = new HashMap<>();

			for (Category category : allCategories) {
				if (category.getParent() != null) {
					Long parentId = category.getParent().getId();
					childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
				}
			}

			return categories.stream()
				.map(category -> buildCategoryTree(category, childrenMap))
				.collect(Collectors.toList());

		} else {
			return buildCategoryHierarchy();
		}
	}

	private List<CategoryDto.Category> buildCategoryHierarchy() {
		List<Category> allCategories = categoryRepository.findAll();

		Map<Long, Category> categoryMap = allCategories.stream()
			.collect(Collectors.toMap(Category::getId, category -> category));

		Map<Long, List<Category>> childrenMap = new HashMap<>();

		for (Category category : allCategories) {
			if (category.getParent() != null) {
				Long parentId = category.getParent().getId();
				childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
			}
		}

		List<Category> rootCategories = allCategories.stream()
			.filter(category -> category.getLevel() == 1)
			.toList();

		return rootCategories.stream()
			.map(root -> buildCategoryTree(root, childrenMap))
			.collect(Collectors.toList());
	}

	private CategoryDto.Category buildCategoryTree(Category category, Map<Long, List<Category>> childrenMap) {
		CategoryDto.Category responseDto = categoryMapper.toCategoryResponse(category);

		List<Category> children = childrenMap.getOrDefault(category.getId(), new ArrayList<>());
		if (!children.isEmpty()) {
			List<CategoryDto.Category> childrenDto = children.stream()
				.map(child -> buildCategoryTree(child, childrenMap))
				.collect(Collectors.toList());
			responseDto.setChildren(childrenDto);
		}

		return responseDto;
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDto.CategoryProducts getCategoryProducts(
		Long categoryId,
		Boolean includeSubcategories,
		PaginationDto.PaginationRequest paginationRequest
	) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(NotFoundException::new);
		CategoryDto.Detail categoryDetail = categoryMapper.toCategoryDetail(category);

		// 상품 조회
		Page<Product> productPage;
		if (Boolean.TRUE.equals(includeSubcategories)) {
			List<Long> categoryIds = collectSubcategoryIds(categoryId);
			productPage = productRepository.findByCategoriesIdIn(categoryIds, paginationRequest.toPageable());
		} else {
			productPage = productRepository.findByCategoriesId(categoryId, paginationRequest.toPageable());
		}

		return CategoryDto.CategoryProducts.builder()
			.category(categoryDetail)
			.items(productPage.getContent().stream().map(productMapper::toProductSummaryDto).toList())
			.pagination(categoryMapper.toPaginationInfo(productPage))
			.build();
	}

	/**
	 * 주어진 카테고리와 모든 하위 카테고리의 ID 목록 수집
	 */
	private List<Long> collectSubcategoryIds(Long rootCategoryId) {
		List<Long> result = new ArrayList<>();
		result.add(rootCategoryId); // 루트 카테고리 포함

		List<Category> allCategories = categoryRepository.findAll();

		Map<Long, Category> categoryMap = allCategories.stream()
			.collect(Collectors.toMap(Category::getId, c -> c));

		Map<Long, List<Category>> childrenMap = new HashMap<>();
		for (Category category : allCategories) {
			if (category.getParent() != null) {
				Long parentId = category.getParent().getId();
				childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
			}
		}

		collectChildCategoryIds(rootCategoryId, childrenMap, result);

		return result;
	}

	/**
	 * 재귀적으로 하위 카테고리 ID 수집
	 */
	private void collectChildCategoryIds(Long categoryId, Map<Long, List<Category>> childrenMap, List<Long> result) {
		List<Category> children = childrenMap.getOrDefault(categoryId, new ArrayList<>());
		for (Category child : children) {
			result.add(child.getId());
			collectChildCategoryIds(child.getId(), childrenMap, result);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDto.Category getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(NotFoundException::new);

		List<Category> allCategories = categoryRepository.findAll();

		Map<Long, List<Category>> childrenMap = new HashMap<>();
		for (Category cat : allCategories) {
			if (cat.getParent() != null) {
				Long parentId = cat.getParent().getId();
				childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(cat);
			}
		}

		return buildCategoryTree(category, childrenMap);
	}
}
