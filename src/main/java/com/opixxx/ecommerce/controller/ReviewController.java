package com.opixxx.ecommerce.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opixxx.ecommerce.controller.dto.ReviewCreateRequest;
import com.opixxx.ecommerce.controller.dto.ReviewUpdateRequest;
import com.opixxx.ecommerce.controller.mapper.ReviewControllerMapper;
import com.opixxx.ecommerce.service.ReviewService;
import com.opixxx.ecommerce.service.dto.PaginationDto;
import com.opixxx.ecommerce.service.dto.ReviewDto;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	private final ReviewControllerMapper mapper;



	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<ApiResponse<ReviewDto.ReviewPage>> getProductReviews(
		@PathVariable Long productId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int perPage,
		@RequestParam(defaultValue = "created_at:desc") String sort,
		@RequestParam(required = false) Integer rating) {

		var paginationRequest = PaginationDto.PaginationRequest.builder()
			.page(page)
			.size(perPage)
			.sort(sort)
			.build();

		// 서비스 호출
		ReviewDto.ReviewPage reviewsPage = reviewService.getProductReviews(productId, rating, paginationRequest);

		return ResponseEntity.ok(ApiResponse.success(reviewsPage, "상품 리뷰를 성공적으로 조회했습니다."));
	}

	@PostMapping("/products/{productId}/reviews")
	public ResponseEntity<ApiResponse<ReviewDto.Review>> createReview(
		@PathVariable Long productId,
		@RequestBody ReviewCreateRequest request) {

		// 인증 관련 로직 (실제로는 Spring Security 등을 통해 구현)
		Long userId = 1L; // 임시로 고정된 사용자 ID 사용

		// DTO 변환
		ReviewDto.CreateRequest serviceDto = mapper.toReviewDtoCreateRequest(request);

		// 서비스 호출
		ReviewDto.Review response = reviewService.createReview(productId, userId, serviceDto);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(response, "리뷰가 성공적으로 등록되었습니다."));
	}

	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse<ReviewDto.Review>> updateReview(
		@PathVariable Long reviewId,
		@RequestBody ReviewUpdateRequest request
	) {

		// 인증 관련 로직 (실제로는 Spring Security 등을 통해 구현)
		Long userId = 1L; // 임시로 고정된 사용자 ID 사용

		// DTO 변환
		ReviewDto.UpdateRequest serviceDto = mapper.toReviewDtoUpdateRequest(request);

		// 서비스 호출
		ReviewDto.Review response = reviewService.updateReview(reviewId, userId, serviceDto);

		return ResponseEntity.ok(ApiResponse.success(response, "리뷰가 성공적으로 수정되었습니다."));
	}

	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {

		// 인증 관련 로직 (실제로는 Spring Security 등을 통해 구현)
		Long userId = 1L; // 임시로 고정된 사용자 ID 사용

		// 서비스 호출
		reviewService.deleteReview(reviewId, userId);

		return ResponseEntity.ok(ApiResponse.success(null, "리뷰가 성공적으로 삭제되었습니다."));
	}
}