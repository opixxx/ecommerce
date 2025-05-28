package com.opixxx.ecommerce.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MainPageDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class MainPage {
		private List<ProductDto.ProductSummary> newProducts;
		private List<ProductDto.ProductSummary> popularProducts;
		private List<FeaturedCategory> featuredCategories;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class FeaturedCategory {
		private Long id;
		private String name;
		private String slug;
		private String imageUrl;
		private Integer productCount;
	}
}