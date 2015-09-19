package com.ksl.myLists.actions;

import com.ksl.myLists.entities.Communication.UserWrappedCommunication;

/**
 * An Action<T> is an object created to perform some action when the instance is applicable to
 * some Communication. 
 * 
 * The Action may optionally generate response elements and will in such cases
 * be provided a response object of type <T>. If this Action does not generate any kind of response,
 * <T> may be defined as Object and null may be provided when calling performFor().
 * 
 * Implementing classes will implement appliesTo() in order to control if this Action will be ran for
 * a given Communication. Note that the applicable ActionType[] from getApplicableActions() is not provided
 * in the method signature. It is expected that some applicability logic will be ActionType agnostic.
 * 
 * Finally, implementing classes will need to define the ActionType[] for which the Action should apply to.
 * It is not specified what to do with this array, and it therefore may be null or empty. It is expected
 * that some actions will either always or never run, though the reasons for this cannot be expected :)
 * 
 * @author Keith Lopez
 *
 * @param <T> - The type of response object this action interacts with. For Twilio, this is a
 * 				TwiMLResponse. For other services such as Plivo it will be different. For actions 
 * 				that do not generate responses, this may be <Object> and any signatures calling for 
 * 				it may be provided null.
 */
public interface Action<T> {
	
	/**
	 * The types of actions for which this Action is applicable.
	 */
	public ActionType[] getApplicableActions();
	
	// Could create a method such as getAction() to return which of the applicable actions
	// this instance is running for <punt because I smell a design flaw>
	
	/**
	 * Given details of this communication, should this action be ran?
	 * Note that implementors will usually find getApplicableActions() necessary.
	 * 
	 * @param communication
	 * @return true if this Action should run for the given communication
	 */
	public boolean appliesTo(UserWrappedCommunication communication);
	
	/**
	 * If this Action runs, will it generate elements to be sent back in a response?
	 * 
	 * If this action does not generate response elements, T may simply be typed as Object.
	 * 
	 * As a communication is passed down the chain of actions, a response object is passed along
	 * to build up a communication that can be sent as a reply to the user. If this method returns 
	 * false, invokers of this class will know that they need not provide a response object and 
	 * may instead provide null to performFor() along with the communication instance.
	 * 
	 * @return true if this Action requires a typed response object
	 */
	public boolean generatesResponseElements();
	
	/**
	 * This method runs the Action for the given Communication. This will usually involve
	 * a service of some kind that has been @Autowired into an implementing class.
	 * 
	 * If generatesResponseElements() is true, a response object of type T must be provided 
	 * for this action to add response elements to. This allows multiple Actions to run on the
	 * same Communication while contributing to the same response. It also allows for actions
	 * to exist in the same chain regardless of their external API.
	 * 
	 * @param communication
	 * @param responseObject instance of type T to collect response elements from any actions that activate
	 */
	public void performFor(UserWrappedCommunication communication, T responseObject);
	
}