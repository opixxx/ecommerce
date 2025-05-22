package com.opixxx.ecommerce.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductListRequest {
	private Integer page = 1;
	private Integer perPage = 10;
	private String sort = "created_at:desc";
	private String status;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private List<Long> category;
	private Long seller;
	private Long brand;
	private Boolean inStock;
	private List<Long> tag;
	private String search;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate createdFrom;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate createdTo;
}
