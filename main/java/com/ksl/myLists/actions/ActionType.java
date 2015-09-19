package com.ksl.myLists.actions;

import lombok.Getter;

import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.entities.User;

/**
 * This type holds the different actions myLists.com can take in response to a Communication.
 * Each type is given the command String which also serves as the pretty name for this type.
 * The logic for determining applicability has been moved here since it is identical for nearly
 * all actions.
 * 
 * @author Keith Lopez
 */
@Getter
public enum ActionType {
	NEW_ITEMS("New Items"),
	LIST("List"), FINISH("Finish"), 
	EDIT("Edit"), MOVE("Move"), DELETE("Delete"),
	MERGE("Merge"), SHARE("Share"),
	ACCEPT_MERGE("Accept"), DENY_MERGE("Deny"),
	ACCOUNT_ACTIVATION("Activate");
	
	// This is the String that is expected to be found at the start of the message in appliesTo().
	// Some action types do not determine applicability in this way, but this field also serves as
	// the pretty name for this ActionType.
	private String command;
	
	private ActionType(String command) {
		this.command = command;
	}
	
	/**
	 * Return true if the message body starts with the Action's command string. 
	 * Note that the command is searched ignoring case, and that the body must start 
	 * with the command String.
	 * 
	 * Code can be added here for types that do not determine applicability based on 
	 * the start of the message.
	 * 
	 * @param communication
	 * @return
	 */
	public boolean appliesTo(UserWrappedCommunication communication) {
		// Communications are considered to be new items iff all other Actions fail to apply
		if(this == NEW_ITEMS) {
			return isNewItemsCommunication(communication);
		}
		
		// Account activation requests are different as well
		if(this == ACCOUNT_ACTIVATION) {
			return isAccountActivationCommunication(communication);
		}
		
		String message = communication.getBody();
		
		// Cannot match if we dont have any message text
		if(message == null || message.isEmpty()) {
			return false;
		}
		
		// Search the message String for the command String ignoring case
		return message.toLowerCase().startsWith(command.toLowerCase());
	}
	
	private static boolean isNewItemsCommunication(UserWrappedCommunication communication) {
		for(ActionType action : ActionType.values()) {
			// Test all types that are not NEW_ITEMS for applicability
			// NEW_ITEMS is not applicable if any other type is
			if(action != NEW_ITEMS && action.appliesTo(communication)) {
				return false;
			}
		}
		
		return true;
	}

	private static boolean isAccountActivationCommunication(UserWrappedCommunication communication) {
		User user = communication.getFromUser();
		
		// If a user is in the workflow of activating an account, any communication is seen as submitting
		// the account activation code
		if(user == null || user.getActivationCode() == null) {
			return false;
		}
		
		return true;
	}
}
