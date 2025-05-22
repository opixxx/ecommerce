package com.opixxx.ecommerce.exception;

public class DuplicateSlugException extends CustomException {

	public DuplicateSlugException() {
		super(ErrorCode.DUPLICATE_SLUG);

	}
}
