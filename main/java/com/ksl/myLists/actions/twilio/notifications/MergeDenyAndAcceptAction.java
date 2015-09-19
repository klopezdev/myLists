package com.ksl.myLists.actions.twilio.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.entities.User;
import com.ksl.myLists.services.ListItemService;
import com.ksl.myLists.services.UserService;

@Component
public class MergeDenyAndAcceptAction extends TwilioNotificationAction {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ListItemService listItemService;
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.ACCEPT_MERGE, ActionType.DENY_MERGE};
	}

	@Override
	public String runAndSetDestinationPhoneNumber(UserWrappedCommunication communication) {
		toPhoneNumber = communication.getFromUser().getMergeFromUser().getSmsNumber();
		
		if(ActionType.ACCEPT_MERGE.appliesTo(communication)) {
			return handleMergeAcceptance(communication);
		} else {
			return handleMergeDeny(communication);
		}
	}

	private String handleMergeDeny(UserWrappedCommunication communication) {
		User user = communication.getFromUser();
		
		user.setMergeFromUser(null);
		userService.saveOrUpdate(user);
		
		StringBuilder notifyDeny = new StringBuilder();
		notifyDeny.append(user.getSmsNumber());
		notifyDeny.append(" has denied your merge request.");
		
		 return notifyDeny.toString();
	}

	private String handleMergeAcceptance(UserWrappedCommunication communication) {
		User user = communication.getFromUser();
		User mergeRequestor = user.getMergeFromUser();
		
		user.setMergeFromUser(null);
		userService.saveOrUpdate(user);
		
		StringBuilder notifyAccept = new StringBuilder();
		notifyAccept.append(user.getSmsNumber());
		notifyAccept.append(" has accepted your merge request.");
		
		listItemService.mergeLists(mergeRequestor, user);
		
		return notifyAccept.toString();
	}
	
}
