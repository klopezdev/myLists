package com.ksl.myLists.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Communication {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String twilioMessageId;
	
	private String fromPhone;
	
	private String toPhone;
	
	private String body;
	
	private Date dateCreated;

	@OneToMany(mappedBy = "communication", fetch = FetchType.LAZY)
	private Set<ListItem> listItems;
	
	/**
	 * We always know the from and to phone numbers, but those may not always have an account.
	 * 
	 * This class will add the from and to user information to Communication while preserving the
	 * to and from phone numbers in Communication.
	 *
	 * @author Keith Lopez
	 */
	@Getter
	public static class UserWrappedCommunication extends Communication {
		private User fromUser, toUser;
		
		public UserWrappedCommunication(Communication communication, User from, User to) {
			super(communication.getId(), 
					communication.getTwilioMessageId(), 
					communication.getFromPhone(),
					communication.getToPhone(),
					communication.getBody(),
					communication.getDateCreated(),
					communication.getListItems()
			);
			
			fromUser = from;
			toUser = to;
		}
	}
	
	
}
