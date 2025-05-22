package com.opixxx.ecommerce.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final int code;
	private final String message;

	public CustomException(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}
}
