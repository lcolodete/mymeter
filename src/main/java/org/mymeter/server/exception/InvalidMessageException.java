package org.mymeter.server.exception;

public class InvalidMessageException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidMessageException(String errorMsg) {
		super(errorMsg);
	}

}
