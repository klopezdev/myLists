package com.ksl.myLists.exceptions;

import com.twilio.sdk.verbs.TwiMLException;


public class BuildTwiMLResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BuildTwiMLResponseException(TwiMLException te) {
		super(te);
	}
	
}
