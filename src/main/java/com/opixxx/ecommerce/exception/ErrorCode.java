package com.opixxx.ecommerce.exception;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	DUPLICATE_SLUG(400, "Duplicate Slug"),
	OPTION_PRODUCT_MISMATCH(400, "Mismatch Option Product"),
	NOT_FOUND_RESOURCE(404, "NotFound Resource");
	private final int code;
	private final String message;

}
