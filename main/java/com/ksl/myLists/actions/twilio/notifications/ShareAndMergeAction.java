package com.ksl.myLists.actions.twilio.notifications;

import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.entities.User;
import com.ksl.myLists.exceptions.InterpretationException;
import com.ksl.myLists.exceptions.UserNotFoundException;
import com.ksl.myLists.services.ListItemService;
import com.ksl.myLists.services.UserService;

@Component
public class ShareAndMergeAction extends TwilioNotificationAction {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ListItemService listItemService;
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.SHARE, ActionType.MERGE};
	}

	@Override
	public String runAndSetDestinationPhoneNumber(UserWrappedCommunication communication) {
		return handleShareOrMergeRequest(communication);
	}
	
	private String handleShareOrMergeRequest(UserWrappedCommunication communication) {
		Matcher matcher = SHARE_OR_MERGE_SMS_NUMBRS_PATTERN.matcher(communication.getBody());
		
		if(matcher.matches()) {
			User fromUser = communication.getFromUser();
			
			toPhoneNumber = matcher.group(2);
			User toUser = userService.findUserBySMSNumber(toPhoneNumber);
			
			if(toUser == null) {
				// TODO: Allow share to send to numbers without a user account
				throw new UserNotFoundException("Unable to find a user at " + toPhoneNumber + ", please verify that this is the correct number and they are a registered user");
			}
			
			if(communication.getBody().toLowerCase().startsWith("merge")) {
				int itemCount = fromUser.getListItems().size();
				
				StringBuilder request = new StringBuilder();
				request.append(fromUser.getSmsNumber());
				request.append(" would like to merge ");
				request.append(itemCount);
				request.append(" items with you, reply with ACCEPT or DENY");
				
				return request.toString();
			} else {
				String list = listItemService.getListForUser(fromUser);
				
				StringBuilder notification = new StringBuilder();
				notification.append(fromUser.getSmsNumber());
				notification.append(" has shared their list with you:\n");
				notification.append(list);
				
				return notification.toString();
			}
		} else {
			throw new InterpretationException("I could not understand your request, did you include a valid phone number in the form of 12101112222?");
		}
	}
}
