package com.demo.webflux.exception;

public class UserAlreadyExists extends ApiException {

	public UserAlreadyExists(final String message) {
		super(message, "USER_ALREADY_EXISTS_EXCEPTION");
	}
}
