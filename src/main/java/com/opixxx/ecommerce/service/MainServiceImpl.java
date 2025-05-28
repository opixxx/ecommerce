package com.opixxx.ecommerce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opixxx.ecommerce.domain.Category;
import com.opixxx.ecommerce.domain.Product;
import com.opixxx.ecommerce.domain.ProductStatus;
import com.opixxx.ecommerce.repository.CategoryRepository;
import com.opixxx.ecommerce.repository.ProductRepository;
import com.opixxx.ecommerce.service.dto.MainPageDto;
import com.opixxx.ecommerce.service.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true)
	public MainPageDto.MainPage getMainPageContents() {

		List<Product> newProducts = productRepository.findTop5ByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE);

		List<Product> popularProducts = productRepository.findTop5PopularProducts();

		List<Category> categories = categoryRepository.findByLevel(1);

		List<Object[]> categoryCountResults = productRepository.countProductsByCategories();

		Map<Long, Long> categoryProductCounts = new HashMap<>();

		for (Object[] result : categoryCountResults) {
			Long categoryId = (Long) result[0];
			Long productCount = ((Number) result[1]).longValue();
			categoryProductCounts.put(categoryId, productCount);
		}

		List<MainPageDto.FeaturedCategory> featuredCategories = categories.stream()
			.map(category -> {
				long productCount = categoryProductCounts.getOrDefault(category.getId(), 0L);
				return MainPageDto.FeaturedCategory.builder()
					.id(category.getId())
					.name(category.getName())
					.slug(category.getSlug())
					.imageUrl(category.getImageUrl())
					.productCount((int)productCount)
					.build();
			})
			.filter(c -> c.getProductCount() > 0)
			.sorted((c1, c2) -> c2.getProductCount() - c1.getProductCount())
			.limit(5)
			.toList();

		return MainPageDto.MainPage.builder()
			.newProducts(newProducts.stream().map(productMapper::toProductSummaryDto).toList())
			.popularProducts(popularProducts.stream().map(productMapper::toProductSummaryDto).toList())
			.featuredCategories(featuredCategories)
			.build();
	}
}
