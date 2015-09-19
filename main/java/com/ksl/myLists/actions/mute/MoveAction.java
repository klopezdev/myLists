package com.ksl.myLists.actions.mute;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.entities.ListItem;
import com.ksl.myLists.entities.User;
import com.ksl.myLists.exceptions.InterpretationException;
import com.ksl.myLists.services.ListItemService;

@Component
public class MoveAction extends MuteAction {

	private static final Pattern MOVE_DIGITS_PATTERN = 
			Pattern.compile("move" + 
							WHITE_SPACE + 
							CAPTURE_ITEM_NUMBER + 
							WHITE_SPACE + 
							CAPTURE_ITEM_NUMBER + 
							WHITE_SPACE + "$", Pattern.CASE_INSENSITIVE);
	
	@Autowired
	private ListItemService listItemService;
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.MOVE};
	}

	@Override
	public void run(UserWrappedCommunication communication) {
		handleMoveRequest(communication);
	}
	
	private void handleMoveRequest(UserWrappedCommunication communication) {
		Matcher matcher = MOVE_DIGITS_PATTERN.matcher(communication.getBody());
		
		if(matcher.matches()) {
			String number1 = matcher.group(1).trim();
			String number2 = matcher.group(2).trim();
			
			int itemNumber1 = Integer.parseInt(number1);
			int itemNumber2 = Integer.parseInt(number2);
			
			User user = communication.getFromUser();
			
			ListItem item1 = listItemService.getListItemByNumber(user.getListItems(), itemNumber1);
			ListItem item2 = listItemService.getListItemByNumber(user.getListItems(), itemNumber2);
			
			item2.setItemNumber(0);
			listItemService.saveOrUpdate(item2, true);
			
			item1.setItemNumber(itemNumber2);
			listItemService.saveOrUpdate(item1, true);
			
			item2.setItemNumber(itemNumber1);
			listItemService.saveOrUpdate(item2);
		} else {
			throw new InterpretationException("I could not understand your move request ex: MOVE 2 4");
		}
	}
}