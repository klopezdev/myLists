package com.ksl.myLists.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksl.myLists.dao.ListItemDao;
import com.ksl.myLists.entities.Communication;
import com.ksl.myLists.entities.ListItem;
import com.ksl.myLists.entities.User;

@Service
public class ListItemService {

	@Autowired
	private ListItemDao listItemDao;
	
	@Autowired
	private UserService userService;
	
	@Transactional
	public User startNewList(User user) {
		user.getListItems().clear();;
		return userService.saveOrUpdate(user);
	}
	
	@Transactional
	public void recordNewListItems(Communication communication, User user) {
		String body = communication.getBody().trim();
		
		int nextItemNumber = getNextItemNumberForUser(user);
		
		for(String item : body.split("\n")) {
			ListItem listItem = new ListItem();
			listItem.setItemNumber(nextItemNumber++);
			listItem.setName(item);
			listItem.setDateCreated(new Date());
			listItem.setCommunication(communication);
			listItem.setUser(user);
			
			listItemDao.saveOrUpdate(listItem, false);
		}
	}
	
	private int getNextItemNumberForUser(User user) {
		if(user.getListItems().isEmpty()) {
			return 1;
		}
		
		ArrayList<ListItem> userList = new ArrayList<>(user.getListItems());
		Collections.sort(userList);
		return userList.get(userList.size() - 1).getItemNumber() + 1;
	}
	
	public ListItem getListItemByNumber(Collection<ListItem> listItems, int number) {
		for(ListItem item : listItems) {
			if(item.getItemNumber() == number) {
				return item;
			}
		}
		
		return null;
	}
	
	@Transactional(readOnly = true)
	public String getListForUser(User user) {
		if(user.getListItems().isEmpty()) {
			return "You have no items on this list yet!";
		}
		
		Set<ListItem> listItems = user.getListItems();
		updateListItemNumbers(listItems);
		
		StringBuilder list = new StringBuilder();
		
		for(ListItem item : listItems) {
			list.append(item.getItemNumber());
			list.append(". ");
			list.append(item.getName());
			list.append("\n");
		}
		
		return list.toString();
	}

	@Transactional
	public boolean deleteListItemForUser(User user, int itemNumber) {
		boolean removed = false;
		
		for(Iterator<ListItem> iterator = user.getListItems().iterator(); iterator.hasNext();) {
			ListItem item = iterator.next();
			
			if(item.getItemNumber() == itemNumber) {
				iterator.remove();
				removed = true;
			}
		}
		
		if(removed) {
			userService.saveOrUpdate(user);
		}
		
		return removed;
	}
	
	@Transactional
	private void updateListItemNumbers(Set<ListItem> listItems) {
		int index = 1;
		for(ListItem item : listItems) {
			if(item.getItemNumber() != index) {
				item.setItemNumber(index);
				listItemDao.saveOrUpdate(item, false);
			}
			
			index++;
		}
	}
	
	@Transactional
	public void saveOrUpdate(ListItem item) {
		saveOrUpdate(item, false);
	}

	@Transactional
	public void saveOrUpdate(ListItem item, boolean flush) {
		listItemDao.saveOrUpdate(item, flush);
	}

	@Transactional
	public void mergeLists(User mergeRequestor, User user) {
		Set<ListItem> mergedList = mergeRequestor.getListItems();
		int startingItemNumber = getNextItemNumberForUser(mergeRequestor);
		
		for(ListItem item : user.getListItems()) {
			ListItem newItem = new ListItem();
			newItem.setName(item.getName());
			newItem.setCommunication(item.getCommunication());
			newItem.setItemNumber(startingItemNumber++);
			newItem.setUser(mergeRequestor);
			newItem.setDateCreated(item.getDateCreated());
			
			mergedList.add(newItem);
		}
		
		userService.saveOrUpdate(mergeRequestor);
		
		user.getListItems().clear();
		
		for(ListItem item : mergeRequestor.getListItems()) {
			ListItem newItem = new ListItem();
			newItem.setName(item.getName());
			newItem.setCommunication(item.getCommunication());
			newItem.setItemNumber(startingItemNumber++);
			newItem.setUser(user);
			newItem.setDateCreated(item.getDateCreated());
			
			user.getListItems().add(newItem);
		}
		
		userService.saveOrUpdate(user);
	}
}
