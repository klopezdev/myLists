package com.ksl.myLists.exceptions;

public class ItemNumberNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ItemNumberNotFoundException(String message) {
		super(message);
	}
}
