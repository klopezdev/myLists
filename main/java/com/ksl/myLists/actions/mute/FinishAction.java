package com.ksl.myLists.actions.mute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.services.ListItemService;

@Component
public class FinishAction extends MuteAction {

	@Autowired
	private ListItemService listItemService; 
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.FINISH};
	}

	@Override
	public void run(UserWrappedCommunication communication) {
		listItemService.startNewList(communication.getFromUser());
	}

}
