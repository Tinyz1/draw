package com.asiainfo.draw.exception;

public class NoMorePrizeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5663148771331449237L;

	public NoMorePrizeException() {
		super();
	}

	public NoMorePrizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoMorePrizeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMorePrizeException(String message) {
		super(message);
	}

	public NoMorePrizeException(Throwable cause) {
		super(cause);
	}

}
