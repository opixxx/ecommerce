package com.opixxx.ecommerce.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, unique = true)
	private Product product;

	@Column(name = "base_price", nullable = false, precision = 19, scale = 2)
	private BigDecimal basePrice;

	@Column(name = "sale_price", precision = 19, scale = 2)
	private BigDecimal salePrice;

	@Column(name = "cost_price", precision = 19, scale = 2)
	private BigDecimal costPrice;

	@Column(length = 3, nullable = false)
	@Builder.Default
	private String currency = "KRW";

	@Column(name = "tax_rate", precision = 5, scale = 2)
	private BigDecimal taxRate;

	// Helper method to calculate discount percentage
	@Transient
	public Integer getDiscountPercentage() {
		if (basePrice == null || salePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
			return 0;
		}

		if (salePrice.compareTo(basePrice) >= 0) {
			return 0;
		}

		BigDecimal discount = basePrice.subtract(salePrice);
		BigDecimal percentage = discount.multiply(new BigDecimal("100")).divide(basePrice, 0, BigDecimal.ROUND_HALF_UP);

		return percentage.intValue();
	}
}
