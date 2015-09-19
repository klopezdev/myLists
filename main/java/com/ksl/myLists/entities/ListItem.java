package com.ksl.myLists.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(
			name = ListItem.FIND_BY_USER_ID,
			query = "SELECT item FROM ListItem item WHERE item.user.id = :userId"
	)
})
public class ListItem implements Comparable<ListItem> {

	public static final String FIND_BY_USER_ID = "ListItem.FIND_BY_USER_ID";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	
	private String name;
	
	// If this ListItem is saved to a list that already exists (has item number 0) then this number MUST be changed
	private int itemNumber = 0;
	
	private Date dateCreated;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "communicationId", nullable = false)
	private Communication communication;

	@Override
	public int compareTo(ListItem other) {
		return Integer.compare(itemNumber, other.getItemNumber());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Communication getCommunication() {
		return communication;
	}

	public void setCommunication(Communication communication) {
		this.communication = communication;
	}
}
