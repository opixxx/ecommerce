package com.opixxx.ecommerce.service.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CategoryDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Category {
		private Long id;
		private String name;
		private String slug;
		private String description;
		private Integer level;
		private String imageUrl;
		@Builder.Default
		private List<Category> children = new ArrayList<>();
	}

	@Getter
	@Builder
	public static class Detail {
		private Long id;
		private String name;
		private String slug;
		private String description;
		private Integer level;
		private String imageUrl;
		private ParentCategory parent;
	}

	@Getter
	@Builder
	public static class ParentCategory {
		private Long id;
		private String name;
		private String slug;
	}

	@Getter
	@Builder
	public static class CategoryProducts {
		private Detail category;
		private List<ProductDto.ProductSummary> items;
		private PaginationDto.PaginationInfo pagination;
	}
}