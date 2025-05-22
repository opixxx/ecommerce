package com.opixxx.ecommerce.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_option_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private String name;

	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;

	@OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ProductOption> options = new ArrayList<>();

	// Helper method
	public void addOption(ProductOption option) {
		option.setOptionGroup(this);
		options.add(option);
	}
}