package com.ksl.myLists.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

@Entity
@NamedQueries({
	@NamedQuery(
			name = User.FIND_BY_SMS_NUMBER,
			query = "SELECT user FROM User user WHERE smsNumber = :smsNumber"
	)
})
public class User {
	
	// NamedQuery names
	public static final String FIND_BY_SMS_NUMBER = "user.findBySMSNumber";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String emailAddress;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
	
	private String smsNumber;
	
	private String activationCode = null;
	
	private Date dateCreated = new Date();
	
	private Date lastLogin = null;

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Sort(type = SortType.NATURAL)
	private Set<ListItem> listItems;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "mergeFromUserId", nullable = true, unique = false)
	private User mergeFromUser;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmsNumber() {
		return smsNumber;
	}

	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Set<ListItem> getListItems() {
		return listItems;
	}

	public void setListItems(HashSet<ListItem> listItems) {
		this.listItems = listItems;
	}

	public User getMergeFromUser() {
		return mergeFromUser;
	}

	public void setMergeFromUser(User mergeFromUser) {
		this.mergeFromUser = mergeFromUser;
	}
}
