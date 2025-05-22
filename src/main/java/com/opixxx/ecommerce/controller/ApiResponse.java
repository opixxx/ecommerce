package com.opixxx.ecommerce.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

	private static final String SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";

	private boolean success;
	private T data;
	private String message;

	public static <T> ApiResponse<T> success(T data, String message) {
		return ApiResponse.<T>builder()
			.success(true)
			.data(data)
			.message(message)
			.build();
	}

	public static <T> ApiResponse<T> success(T data) {
		return success(data, SUCCESS_MESSAGE);
	}

	public static ApiResponse<Void> success(String message) {
		return ApiResponse.<Void>builder()
			.success(true)
			.message(message)
			.build();
	}
}