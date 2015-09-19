package com.ksl.myLists.services.external;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksl.myLists.actions.Action;
import com.ksl.myLists.entities.Communication;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.exceptions.UserNotFoundException;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.MessageList;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@Service
public class TwilioSMSService {
	
	public static String ACCOUNT_SID; 
	public static String AUTH_TOKEN; 
	static {
		ACCOUNT_SID = System.getProperty("TWILIO_ACCOUNT_SID");
		AUTH_TOKEN = System.getProperty("TWILIO_AUTH_TOKEN");
	}

	@Autowired
	private List<Action> availableActions;
	
	@Transactional
	public String handleInboundCommunication(UserWrappedCommunication communication) throws TwiMLException {
		TwiMLResponse instructions = new TwiMLResponse();
		
		try {
			if(communication.getFromUser() == null) {
				throw new UserNotFoundException("A user could not be found for your phone number! Please visit http://KeithLopez.info/myLists to get started :)");
			}
			
			for(Action availableAction : availableActions) {
				if(availableAction.appliesTo(communication)) {
					availableAction.performFor(communication, instructions);
				}
			}
		} catch(Exception e) {
			e.printStackTrace(System.err);
			instructions.append(new com.twilio.sdk.verbs.Message(e.getMessage()));
		}
		
		System.out.println("Instructions are: " + instructions.toXML());
		
		return instructions.toEscapedXML();
	}

	
	public static void sendSingleMessageToOneReceiver(String from, String to, String body) throws TwilioRestException {
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
		 
		 // Build the parameters 
		 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		 params.add(new BasicNameValuePair("To", to)); 
		 params.add(new BasicNameValuePair("From", from)); 
		 params.add(new BasicNameValuePair("Body", body));   
	 
		 MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
		 Message message = messageFactory.create(params); 
		 System.out.println(message.getSid()); 
	}
	
	public static List<Communication> getAllMessagesForAccount() {
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
		
		MessageList messageList = client.getAccount().getMessages();
		List<Communication> messages = new ArrayList<>();
		
		try {
			for (Iterator<Message> iterator = messageList.iterator(); iterator.hasNext();) { 
				Message message = iterator.next();
				
				Communication data = new Communication();
				data.setFromPhone(message.getFrom());
				data.setToPhone(message.getTo());
				data.setBody(message.getBody());
				data.setDateCreated(message.getDateSent());
				
				messages.add(data);
			} 	
		} catch(NoSuchElementException nsee) {
			System.err.println("Twilio get accounts api still not working https://github.com/twilio/twilio-java/issues/204");
		}
		 
		return messages;
	}
}