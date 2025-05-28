package com.opixxx.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opixxx.ecommerce.service.MainService;
import com.opixxx.ecommerce.service.dto.MainPageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;

	@GetMapping
	public ResponseEntity<ApiResponse<MainPageDto.MainPage>> getMainPageContents() {
		return ResponseEntity.ok(
			ApiResponse.success(
				mainService.getMainPageContents(),
				"메인 페이지 상품 목록을 성공적으로 조회했습니다."
			)
		);
	}
}