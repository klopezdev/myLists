package com.ksl.myLists.actions.twilio;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.services.ListItemService;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.Verb;

@Component
public class TwilioListAction extends TwilioAction {
	
	@Autowired
	private ListItemService listItemService;
	
	@Override
	public List<? extends Verb> run(UserWrappedCommunication communication) {
		String list = listItemService.getListForUser(communication.getFromUser());
		return Collections.singletonList(new Message(list));
	}

	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.LIST};
	}

}
