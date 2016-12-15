package com.wooplr.base.exception;

public class SpireException extends Exception{

	private static final long serialVersionUID = -2757992149932989353L;

	public SpireException() {
	}

	public SpireException(String message) {
		super(message);
	}

	public SpireException(Throwable cause) {
		super(cause);
	}

	public SpireException(String message, Throwable cause) {
		super(message, cause);
	}

}
