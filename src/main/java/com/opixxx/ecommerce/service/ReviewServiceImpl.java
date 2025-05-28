package com.opixxx.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opixxx.ecommerce.domain.Product;
import com.opixxx.ecommerce.domain.Review;
import com.opixxx.ecommerce.domain.User;
import com.opixxx.ecommerce.exception.AccessDeniedException;
import com.opixxx.ecommerce.exception.NotFoundException;
import com.opixxx.ecommerce.repository.ProductRepository;
import com.opixxx.ecommerce.repository.ReviewRepository;
import com.opixxx.ecommerce.repository.UserRepository;
import com.opixxx.ecommerce.service.dto.PaginationDto;
import com.opixxx.ecommerce.service.dto.ReviewDto;
import com.opixxx.ecommerce.service.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final ReviewMapper reviewMapper;

	@Override
	@Transactional(readOnly = true)
	public ReviewDto.ReviewPage getProductReviews(
		Long productId,
		Integer rating,
		PaginationDto.PaginationRequest paginationRequest
	) {

		productRepository.findById(productId)
			.orElseThrow(NotFoundException::new);

		List<Review> allReviews = reviewRepository.findAllByProductId(productId);

		ReviewDto.ReviewSummary summary = reviewMapper.toReviewSummary(allReviews);

		Page<Review> reviewPage;
		if (rating != null) {
			reviewPage = reviewRepository.findByProductIdAndRating(productId, rating, paginationRequest.toPageable());
		} else {
			reviewPage = reviewRepository.findByProductId(productId, paginationRequest.toPageable());
		}

		List<ReviewDto.Review> reviewResponses = reviewPage.getContent().stream()
			.map(reviewMapper::toReviewDto)
			.collect(Collectors.toList());

		PaginationDto.PaginationInfo paginationInfo = PaginationDto.PaginationInfo.builder()
			.totalItems((int) reviewPage.getTotalElements())
			.totalPages(reviewPage.getTotalPages())
			.currentPage(reviewPage.getNumber() + 1) // 0-based to 1-based
			.perPage(reviewPage.getSize())
			.build();

		return ReviewDto.ReviewPage.builder()
			.items(reviewResponses)
			.summary(summary)
			.pagination(paginationInfo)
			.build();
	}

	@Override
	@Transactional
	public ReviewDto.Review createReview(Long productId, Long userId, ReviewDto.CreateRequest request) {

		Product product = productRepository.findById(productId)
			.orElseThrow(NotFoundException::new);

		User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

		Review review = Review.builder()
			.product(product)
			.user(user)
			.rating(request.getRating())
			.title(request.getTitle())
			.content(request.getContent())
			.verifiedPurchase(true)
			.helpfulVotes(0)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		review = reviewRepository.save(review);

		return reviewMapper.toReviewDto(review);
	}

	@Override
	@Transactional
	public ReviewDto.Review updateReview(Long reviewId, Long userId, ReviewDto.UpdateRequest request) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(NotFoundException::new);

		if (!review.getUser().getId().equals(userId)) {
			throw new AccessDeniedException();
		}

		if (request.getRating() != null) {
			review.setRating(request.getRating());
		}

		if (request.getTitle() != null) {
			review.setTitle(request.getTitle());
		}

		if (request.getContent() != null) {
			review.setContent(request.getContent());
		}

		review.setUpdatedAt(LocalDateTime.now());

		review = reviewRepository.save(review);

		return reviewMapper.toReviewDto(review);
	}

	@Override
	@Transactional
	public void deleteReview(Long reviewId, Long userId) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(NotFoundException::new);

		if (!review.getUser().getId().equals(userId)) {
			throw new AccessDeniedException();
		}

		reviewRepository.delete(review);
	}
}