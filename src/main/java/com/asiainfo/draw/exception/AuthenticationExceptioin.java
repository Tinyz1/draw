package com.asiainfo.draw.exception;

public class AuthenticationExceptioin extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -225852622717111195L;

	public AuthenticationExceptioin() {
		super();
	}

	public AuthenticationExceptioin(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthenticationExceptioin(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationExceptioin(String message) {
		super(message);
	}

	public AuthenticationExceptioin(Throwable cause) {
		super(cause);
	}

}
