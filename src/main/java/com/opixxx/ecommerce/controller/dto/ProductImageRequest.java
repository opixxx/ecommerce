package com.opixxx.ecommerce.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductImageRequest {
	private String url;
	private String altText;
	private boolean isPrimary;
	private Integer displayOrder;
	private Long optionId;
}