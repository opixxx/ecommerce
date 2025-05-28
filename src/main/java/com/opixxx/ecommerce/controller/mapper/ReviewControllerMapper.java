package com.opixxx.ecommerce.controller.mapper;

import org.springframework.stereotype.Component;

import com.opixxx.ecommerce.controller.dto.ReviewCreateRequest;
import com.opixxx.ecommerce.controller.dto.ReviewUpdateRequest;
import com.opixxx.ecommerce.service.dto.ReviewDto;

@Component
public class ReviewControllerMapper {

	public ReviewDto.CreateRequest toReviewDtoCreateRequest(ReviewCreateRequest request) {
		return ReviewDto.CreateRequest.builder()
			.rating(request.getRating())
			.title(request.getTitle())
			.content(request.getContent())
			.build();
	}

	public ReviewDto.UpdateRequest toReviewDtoUpdateRequest(ReviewUpdateRequest request) {
		return ReviewDto.UpdateRequest.builder()
			.rating(request.getRating())
			.content(request.getContent())
			.build();
	}

}
