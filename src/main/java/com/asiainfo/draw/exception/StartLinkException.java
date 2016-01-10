package com.asiainfo.draw.exception;

/**
 * 环节开始异常
 * 
 * @author yecl
 *
 */
public class StartLinkException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4272242756130771937L;

	public StartLinkException() {
		super();
	}

	public StartLinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StartLinkException(String message, Throwable cause) {
		super(message, cause);
	}

	public StartLinkException(String message) {
		super(message);
	}

	public StartLinkException(Throwable cause) {
		super(cause);
	}

}
