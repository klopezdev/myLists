package com.ksl.myLists.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ksl.myLists.entities.Communication;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.services.CommunicationService;
import com.ksl.myLists.services.external.TwilioSMSService;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.TwiMLException;

@Controller
public class TwilioController {

	
	@Autowired
	private CommunicationService communicationService;
	
	@Autowired
	private TwilioSMSService twilioSMSService;
	
	@RequestMapping(value = "/list/inbound", method = RequestMethod.POST, produces = "application/xml")
	@ResponseBody
	public String inboundListCommunication(
			@RequestParam(value = "From") String from,
			@RequestParam(value = "To") String to,
			@RequestParam(value = "Body") String body,
			@RequestParam(value = "MessageSid") String twilioMessageId) throws TwiMLException {
		
		//Record all incoming communications before doing anything with them
		System.out.println("New inbound list communication from " + from + " with body: \"" + body + "\"");
		
		from = from.substring(1);
		to = to.substring(1);
		
		Communication communication = new Communication();
		communication.setFromPhone(from);
		communication.setToPhone(to);
		communication.setBody(body);
		communication.setTwilioMessageId(twilioMessageId);
		communication.setDateCreated(new Date());
		
		communication = communicationService.recordCommunication(communication);
		UserWrappedCommunication communicationWithUsers = communicationService.getCommunicationWithUsers(communication);
		
		return twilioSMSService.handleInboundCommunication(communicationWithUsers);
	}

	@RequestMapping(value = "/twilio/message/send", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void sendTwilioSMSToSingleReceiver(String from, String to, String message) throws TwilioRestException {
		TwilioSMSService.sendSingleMessageToOneReceiver(from, to, message);
	}
	
	@RequestMapping(value = "/twilio/messages", method = RequestMethod.GET)
	@ResponseBody
	public List<Communication> getAllMessages() {
		List<Communication> messages = TwilioSMSService.getAllMessagesForAccount();
		return messages;
	}
}
