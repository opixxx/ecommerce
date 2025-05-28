package com.opixxx.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.opixxx.ecommerce.domain.Product;
import com.opixxx.ecommerce.domain.ProductStatus;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsBySlug(String slug);

	@EntityGraph(attributePaths = {"price", "brand", "seller"})
	Page<Product> findAll(Specification<Product> spec, Pageable pageable);

	Page<Product> findByCategoriesIdIn(List<Long> categoryIds, Pageable pageable);

	Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);

	List<Product> findTop5ByStatusOrderByCreatedAtDesc(ProductStatus productStatus);

	@Query("""
		SELECT p
	   	FROM Product p
    	JOIN p.reviews r
    	WHERE p.status = 'ACTIVE'
    	GROUP BY p
    	ORDER BY AVG(r.rating) DESC, COUNT(r) DESC
    	LIMIT 5
    """)
	List<Product> findTop5PopularProducts();

	@Query("""
		SELECT c.id, COUNT(p)
		FROM Product p
		JOIN p.categories c
		WHERE p.status = 'ACTIVE'
		GROUP BY c.id
	""")
	List<Object[]> countProductsByCategories();
}