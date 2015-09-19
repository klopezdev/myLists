package com.ksl.myLists.actions;

import java.util.regex.Pattern;

import com.ksl.myLists.entities.Communication.UserWrappedCommunication;

/**
 * Base class for all Action<T>.
 * 
 * @author Keith Lopez
 */
public abstract class BaseAction<T> implements Action<T> {
	
	/*
	 * These regular expression elements can be used by extending classes to build regular expressions
	 * for use in the appliesTo method
	 */
	public static final String WHITE_SPACE = "\\s*";
	public static final String CAPTURE_PHONE_NUMBER = "(1\\d{10})";
	public static final String CAPTURE_ITEM_NUMBER = "(\\d{1,})";
	// Capture the phone number to share or merge with in group 2
	public static final Pattern SHARE_OR_MERGE_SMS_NUMBRS_PATTERN = Pattern.compile("(MERGE|SHARE)" + WHITE_SPACE + CAPTURE_PHONE_NUMBER, Pattern.CASE_INSENSITIVE);
	
	
	/**
	 * Most actions will care about the types they are applicable to. This method abstracts that
	 * logic allowing extending classes to call super.appliesTo() to invoke it. If an Action<T>
	 * does not care about ActionType then it need not call this implementation.
	 * 
	 * @param communication that can be passed to ActionType.appliesTo() for evaluation
	 */
	@Override
	public boolean appliesTo(UserWrappedCommunication communication) {
		for(ActionType action : getApplicableActions()) {
			// Applicable if any type applies
			if(action.appliesTo(communication)) {
				return true;
			}
		}
		
		return false;
	}
	
}
