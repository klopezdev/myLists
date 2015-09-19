package com.ksl.myLists.actions.mute;

import com.ksl.myLists.actions.BaseAction;
import com.ksl.myLists.entities.Communication.UserWrappedCommunication;

/**
 * Action<T> abstraction representing actions that do not generate response elements.
 * This class is intended to simplify development of actions for which generateResponseElements()
 * return false.
 * 
 * @author Keith Lopez
 */
public abstract class MuteAction extends BaseAction<Object> {

	/**
	 * The logic of this action! 
	 * 
	 * The implementation of this method will execute the logic this Action represents.
	 * 
	 * Because no response elements are generated, the responseObject is not passed.
	 */
	public abstract void run(UserWrappedCommunication communication);
	
	/**
	 * Mute actions do not generate response elements and therefore do not work with a response object.
	 */
	@Override
	public boolean generatesResponseElements() {
		return false;
	}

	/**
	 * Because this class does not generate a response, simply call run().
	 */
	@Override
	public void performFor(UserWrappedCommunication communication, Object responseObject) {
		run(communication);
	}
}
