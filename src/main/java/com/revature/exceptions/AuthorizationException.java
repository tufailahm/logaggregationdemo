package com.revature.exceptions;

public abstract class AuthorizationException extends RuntimeException {

	protected AuthorizationException() {
		super();
	}

	protected AuthorizationException(String message) {
		super(message);
	}

	protected AuthorizationException(Throwable cause) {
		super(cause);
	}

	protected AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	protected AuthorizationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
