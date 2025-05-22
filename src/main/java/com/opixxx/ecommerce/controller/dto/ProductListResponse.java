package com.opixxx.ecommerce.controller.dto;

import java.util.List;

import com.opixxx.ecommerce.service.dto.PaginationDto;
import com.opixxx.ecommerce.service.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {
	private List<ProductDto.ProductSummary> items;
	private PaginationDto.PaginationInfo pagination;
}
