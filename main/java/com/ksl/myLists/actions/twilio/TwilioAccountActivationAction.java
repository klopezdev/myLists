package com.ksl.myLists.actions.twilio;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.services.UserService;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.Verb;

@Component
public class TwilioAccountActivationAction extends TwilioAction {
	
	@Autowired
	private UserService userService;
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.ACCOUNT_ACTIVATION};
	}

	@Override
	public List<? extends Verb> run(UserWrappedCommunication communication) {
		boolean activated = userService.handleAccountActivationRequest(communication, communication.getFromUser());
		
		if(activated) {
			return Collections.singletonList(new Message("Welcome to the power of myLists.com!"));
		} else {
			return Collections.singletonList(new Message("This access code is incorrect! Please try again..."));
		}
	}

}
