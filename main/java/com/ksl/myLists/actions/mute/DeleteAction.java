package com.ksl.myLists.actions.mute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.exceptions.InterpretationException;
import com.ksl.myLists.exceptions.ItemNumberNotFoundException;
import com.ksl.myLists.services.ListItemService;

@Component
public class DeleteAction extends MuteAction {

	private static final Pattern DELETE_DIGITS_PATTERN = Pattern.compile(WHITE_SPACE + "(\\d{1,}){1,}");
	
	@Autowired
	private ListItemService listItemService;
	
	@Override
	public ActionType[] getApplicableActions() {
		return new ActionType[]{ActionType.DELETE};
	}

	@Override
	public void run(UserWrappedCommunication communication) {
		handleDeleteRequest(communication);
	}
	
	private void handleDeleteRequest(UserWrappedCommunication communication) {
		Matcher matcher = DELETE_DIGITS_PATTERN.matcher(communication.getBody());
		
		List<Integer> delete = new ArrayList<Integer>();
		while(matcher.find()) {
			String number = matcher.group().trim();
			delete.add(Integer.parseInt(number));
		} 
		
		if(!delete.isEmpty()) {
			for(Iterator<Integer> iterator = delete.iterator(); iterator.hasNext();) {
				int itemNumber = iterator.next();
				if(listItemService.deleteListItemForUser(communication.getFromUser(), itemNumber)) {
					iterator.remove();
				}
			}
		}
		else {
			 throw new InterpretationException("I could not understand your delete request ex: 'DELETE 4 7 11 36'");
		}
		
		if(!delete.isEmpty()) {
			StringBuffer notFound = new StringBuffer("Unable to find the following items to delete: ");
			for(Integer itemNumber : delete) {
				notFound.append(itemNumber);
				notFound.append(" ");
			}
			
			throw new ItemNumberNotFoundException(notFound.toString());
		}
	}

}
