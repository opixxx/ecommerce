package com.opixxx.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.opixxx.ecommerce.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findByProductId(Long productId, Pageable pageable);

	Page<Review> findByProductIdAndRating(Long productId, Integer rating, Pageable pageable);

	List<Review> findAllByProductId(Long productId);

	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
	Double calculateAverageRatingByProductId(@Param("productId") Long productId);

	Long countByProductId(Long productId);
}