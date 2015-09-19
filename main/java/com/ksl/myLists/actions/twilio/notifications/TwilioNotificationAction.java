package com.ksl.myLists.actions.twilio.notifications;

import com.ksl.myLists.actions.BaseAction;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.exceptions.SMSNotificationException;
import com.ksl.myLists.services.external.TwilioSMSService;
import com.twilio.sdk.TwilioRestException;

/**
 * TwilioNotificationAction are actions that do not generate response elements, but do as a
 * result of performing their action, send a notification message to a single receiver.
 * 
 * The notification text and phone number to send to are both determined by runAndSetDestinationPhoneNumber().
 * This method must at some point set the class variable toPhoneNumber or an exception will be thrown
 * by TwilioSMSService.sendSingleMessageToOneReceiver(). The returned String from this method will be used
 * as the body of the SMS notification sent.
 * 
 * @author Keith Lopez
 *
 */
public abstract class TwilioNotificationAction extends BaseAction<Object> {

	protected String toPhoneNumber;

	/**
	 * The notification text and phone number to send to are both determined by this method.
	 * This method must at some point set the class variable toPhoneNumber or an exception will be thrown
	 * later when trying to send a notification message to nobody. The returned String from this method 
	 * will be used as the body of the SMS notification sent.
	 * 
	 * @param communication
	 * @return String to be sent as notification to toPhoneNumber
	 */
	public abstract String runAndSetDestinationPhoneNumber(UserWrappedCommunication communication);
	
	@Override
	public boolean generatesResponseElements() {
		return false;
	}

	/**
	 * Run this actions logic and send the resulting notification String as a message to toPhoneNumber.
	 * 
	 * Any errors encountered while sending the notification will be thrown as SMSNotificationException
	 * to distinguish them from other myLists specific exceptions.
	 * 
	 * @param communication
	 * @param responseObject which is ignored so may be null
	 */
	@Override
	public void performFor(UserWrappedCommunication communication, Object responseObject) {
		String notification = runAndSetDestinationPhoneNumber(communication);
		
		try {
			TwilioSMSService.sendSingleMessageToOneReceiver(communication.getToPhone(), toPhoneNumber, notification);
		} catch(TwilioRestException tre) {
			throw new SMSNotificationException("Error notifying " + toPhoneNumber + " of your response!");
		}
	}
}
