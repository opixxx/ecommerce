package com.opixxx.ecommerce.controller;

public record ErrorResponse(
	boolean success,
	ErrorDetails error
) {

	public record ErrorDetails(
		int code,
		String message

	) {}

	public static ErrorResponse of(int code, String message) {
		return new ErrorResponse(
			false,
			new ErrorDetails(code, message)
		);
	}

}
