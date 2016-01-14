package com.asiainfo.draw.exception;

/**
 * —È÷§¬Î¥ÌŒÛ“Ï≥£
 * 
 * @author yecl
 *
 */
public class EnterNumberErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3725176003514289055L;

	public EnterNumberErrorException() {
		super();
	}

	public EnterNumberErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EnterNumberErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public EnterNumberErrorException(String message) {
		super(message);
	}

	public EnterNumberErrorException(Throwable cause) {
		super(cause);
	}

}
