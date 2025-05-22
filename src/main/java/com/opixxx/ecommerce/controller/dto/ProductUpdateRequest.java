package com.opixxx.ecommerce.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {
	private String name;
	private String slug;
	private String shortDescription;
	private String fullDescription;
	private Long sellerId;
	private Long brandId;
	private String status;

	private ProductCreateRequest.ProductDetailDto detail;
	private ProductCreateRequest.ProductPriceDto price;
	private List<ProductCreateRequest.ProductCategoryDto> categories;
	private List<Long> tagIds;
}