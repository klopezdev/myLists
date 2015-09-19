package com.ksl.myLists.actions.mute;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.entities.ListItem;
import com.ksl.myLists.exceptions.InterpretationException;
import com.ksl.myLists.exceptions.ItemNumberNotFoundException;
import com.ksl.myLists.services.ListItemService;

@Component
public class EditAction extends MuteAction {

	private static final Pattern EDIT_DIGIT_NEW_NAME_PATTERN = Pattern.compile("edit" + WHITE_SPACE + CAPTURE_ITEM_NUMBER + WHITE_SPACE + "(\\S.{1,})$", Pattern.CASE_INSENSITIVE);
	
	@Autowired
	private ListItemService listItemService;
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.EDIT};
	}

	@Override
	public void run(UserWrappedCommunication communication) {
		handleEditRequest(communication);
	}
	
	private void handleEditRequest(UserWrappedCommunication communication) {
		Matcher matcher = EDIT_DIGIT_NEW_NAME_PATTERN.matcher(communication.getBody());
		
		if(matcher.matches()) {
			String number = matcher.group(1).trim();
			String newItem = matcher.group(2);
			
			int itemNumber = Integer.parseInt(number);
			
			ListItem item = listItemService.getListItemByNumber(communication.getFromUser().getListItems(), itemNumber);
			
			if(item != null) {
				item.setName(newItem);
				listItemService.saveOrUpdate(item);
			} else {
				throw new ItemNumberNotFoundException("Unable to find an item to edit at number " + itemNumber);
			}
		} else {
			throw new InterpretationException("I could not understand your edit request ex: EDIT 2 Should now say this");
		}
	}

}
