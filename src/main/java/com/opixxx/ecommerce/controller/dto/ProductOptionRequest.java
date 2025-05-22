package com.opixxx.ecommerce.controller.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductOptionRequest {
	private String name;
	private BigDecimal additionalPrice;
	private String sku;
	private Integer stock;
	private Integer displayOrder;
}
