package com.opixxx.ecommerce.exception;

public class OptionProductMismatchException extends CustomException {

	public OptionProductMismatchException() {
		super(ErrorCode.OPTION_PRODUCT_MISMATCH);
	}
}
