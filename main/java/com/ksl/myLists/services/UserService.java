package com.ksl.myLists.services;

import java.util.Random;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksl.myLists.dao.UserDao;
import com.ksl.myLists.entities.Communication;
import com.ksl.myLists.entities.User;
import com.ksl.myLists.exceptions.ajax.DuplicateUserException;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	public User saveOrUpdate(User user) {
		try {
			return userDao.saveOrUpdate(user);
		} catch(PersistenceException pe) {
			if(pe.getMessage().contains("Duplicate entry")) {
				throw new DuplicateUserException("A user already exists for the email address " + user.getEmailAddress());
			}
			
			throw pe;
		}
	}
	
	public User findUserBySMSNumber(String smsNumber) {
		return userDao.findUserBySMSNumber(smsNumber);
	}

	public String generateActivationCode() {
		Random generator = new Random();
		StringBuilder activationCode = new StringBuilder();
		
		for(int i = 0; i < 8; i++) {
			int number = generator.nextInt(10);
			activationCode.append(number);
			
			if(i == 3 || i == 6) {
				activationCode.append(" ");
			}
		}
		
		return activationCode.toString();
	}
	
	public boolean handleAccountActivationRequest(Communication communication, User user) {
		if(communication.getBody().trim().equals(user.getActivationCode())) {
			user.setActivationCode(null);
			saveOrUpdate(user);
			return true;
		} else {
			return false;
		}
	}
}
