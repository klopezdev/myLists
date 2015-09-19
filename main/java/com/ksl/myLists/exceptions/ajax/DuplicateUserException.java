package com.ksl.myLists.exceptions.ajax;

import org.apache.http.HttpStatus;

public class DuplicateUserException extends RuntimeException implements AjaxException {

	private static final long serialVersionUID = 1L;

	public DuplicateUserException(String message) {
		super(message);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.SC_NOT_ACCEPTABLE;
	}
}
