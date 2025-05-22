package com.opixxx.ecommerce.exception;

public class NotFoundException extends CustomException{
	public NotFoundException() {
		super(ErrorCode.NOT_FOUND_RESOURCE);
	}
}
