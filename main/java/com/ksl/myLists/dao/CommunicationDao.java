package com.ksl.myLists.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.ksl.myLists.entities.Communication;

@Repository
public class CommunicationDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Communication saveOrUpdate(Communication communication) {
		return entityManager.merge(communication);
	}
	
}
