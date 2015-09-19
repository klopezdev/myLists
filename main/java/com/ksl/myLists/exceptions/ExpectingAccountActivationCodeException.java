package com.ksl.myLists.exceptions;

public class ExpectingAccountActivationCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Expecting account activation code but message does not match pattern";
	
	public ExpectingAccountActivationCodeException() {
		super(MESSAGE);
	}

}
