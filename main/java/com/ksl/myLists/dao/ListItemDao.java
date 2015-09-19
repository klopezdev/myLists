package com.ksl.myLists.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.ksl.myLists.entities.ListItem;

@Repository
public class ListItemDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public ListItem saveOrUpdate(ListItem listItem, boolean flush) {
		ListItem item = entityManager.merge(listItem);
		
		if(flush) {
			entityManager.flush();
		}
		
		return item;
	}
	
}
