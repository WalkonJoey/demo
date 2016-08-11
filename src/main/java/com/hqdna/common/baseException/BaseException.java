package com.hqdna.common.baseException;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -516944531649030805L;

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}
}