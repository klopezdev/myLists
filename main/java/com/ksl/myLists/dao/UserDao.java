package com.ksl.myLists.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.ksl.myLists.entities.User;

@Repository
public class UserDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public User saveOrUpdate(User user) {
		return entityManager.merge(user);
	}
	
	public User findUserBySMSNumber(String smsNumber) {
		List<User> users = 
				entityManager.createNamedQuery(User.FIND_BY_SMS_NUMBER, User.class)
				.setParameter("smsNumber", smsNumber)
				.getResultList();
		
		if(users.size() > 1) {
			throw new IllegalStateException("Found more than one user with the sms phone number: " + smsNumber);
		} else {
			return users.isEmpty() ? null : users.get(0);
		}
	}
	
}
