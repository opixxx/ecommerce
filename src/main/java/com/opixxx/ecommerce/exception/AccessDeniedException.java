package com.opixxx.ecommerce.exception;

public class AccessDeniedException extends CustomException{
	public AccessDeniedException() {
		super(ErrorCode.ACCESS_DENIED);
	}
}
