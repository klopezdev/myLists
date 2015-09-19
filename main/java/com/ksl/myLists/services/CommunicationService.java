package com.ksl.myLists.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksl.myLists.actions.ActionType;
import com.ksl.myLists.dao.CommunicationDao;
import com.ksl.myLists.entities.Communication;
import com.ksl.myLists.entities.User;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;

@Service
public class CommunicationService {

	@Autowired
	private CommunicationDao communicationDao;
	
	@Autowired
	private ListItemService listItemService;
	
	@Autowired
	private UserService userService;
	
	@Transactional
	public Communication recordCommunication(Communication communication) {
		return communicationDao.saveOrUpdate(communication);
	}
	
	public UserWrappedCommunication getCommunicationWithUsers(Communication communication) {
		String from = communication.getFromPhone();
		String to = communication.getToPhone();
		
		User fromUser = userService.findUserBySMSNumber(from);
		User toUser = userService.findUserBySMSNumber(to);
		
		return new UserWrappedCommunication(communication, fromUser, toUser);
	}
	
}
