package com.demo.webflux.exception;

public class AuthException extends ApiException {

	public AuthException(final String message, final String errorCode) {
		super(message, errorCode);
	}
}
