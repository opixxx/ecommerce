package com.opixxx.ecommerce.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(length = 1000)
	private String description;

	@Column(name = "logo_url")
	private String logoUrl;

	@Column(precision = 3, scale = 2)
	private BigDecimal rating;

	@Column(name = "contact_email")
	private String contactEmail;

	@Column(name = "contact_phone")
	private String contactPhone;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "seller", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Builder.Default
	private List<Product> products = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}