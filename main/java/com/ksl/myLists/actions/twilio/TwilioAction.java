package com.ksl.myLists.actions.twilio;

import java.util.List;

import com.ksl.myLists.actions.BaseAction;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;
import com.ksl.myLists.exceptions.BuildTwiMLResponseException;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.Verb;

/**
 * An Action to be ran when the Communication to be acted upon is from the Twilio SMS service.
 * Communication from other sources will be ignored by TwilioActions. 
 * 
 * Twilio Actions use a TwiMLResponse object to append response elements to.
 * 
 * TwilioAction.appliesTo() will only return true if the communication is from the
 * Twilio service. Implementing classes should always call super.appliesTo() since Actions that 
 * do not care about source should instead extend MuteAction.
 * 
 * @author Keith Lopez
 */
public abstract class TwilioAction extends BaseAction<TwiMLResponse> {

	/**
	 * Return true if the communication is found to have come from Twilio and 
	 * BaseAction.appliesTo() also returns true. 
	 * 
	 * @param communication - The communication being acted upon
	 */
	@Override
	public boolean appliesTo(UserWrappedCommunication communication) {
		if(communication.getTwilioMessageId() == null) {
			return false;
		}
		
		return super.appliesTo(communication);
	}
	
	/**
	 * The logic of this action! 
	 * 
	 * The implementation of this method will execute the logic this Action represents. 
	 * TwilioActions respond with a TwiMLResponse object which is composed of Verbs. 
	 * 
	 * This method returns a List of Verbs which the performFor() method appends to the 
	 * provided TwiMLResponse instance.
	 * 
	 * @param communication
	 * @return a List of Verb elements to be appended (in order) to the TwiMLResponse object
	 */
	public abstract List<? extends Verb> run(UserWrappedCommunication communication);
	
	/**
	 * All TwilioActions will generate response elements. If an Action does not generate 
	 * response elements, it should extend MuteAction instead of TwilioAction.
	 */
	@Override
	public boolean generatesResponseElements() {
		return true;
	}
	
	/**
	 * Run this TwilioAction and append the resulting List of Verb elements to the TwiMLResponse
	 * instance. Errors thrown while appending Verb elements to the response object will be thrown
	 * as BuildTwiMLResponseException to distinguish them from other myLists specific exceptions.
	 */
	@Override
	public void performFor(UserWrappedCommunication communication, TwiMLResponse responseObject) {
		List<? extends Verb> responseActions = run(communication);
		
		try {
			for(Verb responseAction : responseActions) {
				responseObject.append(responseAction);
			}
		} catch (TwiMLException e) {
			responseObject = new TwiMLResponse();
			
			throw new BuildTwiMLResponseException(e);
		}
	}
}
