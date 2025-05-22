package com.opixxx.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opixxx.ecommerce.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}