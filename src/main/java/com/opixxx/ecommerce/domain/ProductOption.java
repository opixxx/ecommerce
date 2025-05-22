package com.opixxx.ecommerce.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_group_id", nullable = false)
	private ProductOptionGroup optionGroup;

	@Column(nullable = false)
	private String name;

	@Column(name = "additional_price", precision = 19, scale = 2)
	private BigDecimal additionalPrice;

	@Column(nullable = false, unique = true)
	private String sku;

	@Column(nullable = false)
	private Integer stock;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	@OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ProductImage> images = new ArrayList<>();
}