package com.opixxx.ecommerce.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(name = "short_description", length = 500)
	private String shortDescription;

	@Column(name = "full_description", columnDefinition = "TEXT")
	private String fullDescription;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	private Seller seller;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;

	// One-to-One relationships
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private ProductDetail detail;

	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private ProductPrice price;

	// Category를 ManyToMany 관계로 직접 참조
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
		name = "product_categories",
		joinColumns = @JoinColumn(name = "product_id"),
		inverseJoinColumns = @JoinColumn(name = "category_id")
	)
	@Builder.Default
	private List<Category> categories = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ProductOptionGroup> optionGroups = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ProductImage> images = new ArrayList<>();

	// Tag도 ManyToMany 관계로 직접 참조
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
		name = "product_tags",
		joinColumns = @JoinColumn(name = "product_id"),
		inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	@Builder.Default
	private List<Tag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Review> reviews = new ArrayList<>();

	// Helper methods
	public void addCategory(Category category) {
		if (!categories.contains(category)) {
			categories.add(category);
		}
	}

	public void addOptionGroup(ProductOptionGroup optionGroup) {
		optionGroup.setProduct(this);
		optionGroups.add(optionGroup);
	}

	public void addImage(ProductImage image) {
		image.setProduct(this);
		images.add(image);
	}

	public void addTag(Tag tag) {
		if (!tags.contains(tag)) {
			tags.add(tag);
		}
	}

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}