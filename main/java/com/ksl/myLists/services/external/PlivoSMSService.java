package com.ksl.myLists.services.external;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.ksl.myLists.utils.PlivoUtils;
import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.message.MessageResponse;
import com.plivo.helper.exception.PlivoException;

@Service
public class PlivoSMSService {

	private static final String PLIVO_AUTH_ID = "MAODI0N2E3YMI4ZDI1MW";
    private static final String PLIVO_AUTH_TOKEN = "NjM5NDgyYzNkMmY2MjgxZjg4OTJiMzgxZjY2ZjE1";
    
    // The URL to which with the status of a sent message is sent
    // This url must be mapped to a myLists controller method that can handle the message status response
//    private static final String MY_LISTS_SMS_STATUS_RESPONSE_URL = "http://keithlopez.info/myLists/plivo/sms/status_response";
    // The method used to call MY_LISTS_SMS_STATUS_RESPONSE_URL
//    private static final String MY_LISTS_SMS_STATUS_RESPONSE_METHOD = "POST";
    /**
     * 
     * @param fromPhoneNumber Sender's phone number with country code
     * @param toPhoneNumber Receiver's phone number with country code
     * @param body message to be sent
     */
	public static void sendSingleSMSToOnReceiver(String fromPhoneNumber, String toPhoneNumber, String body) {
        RestAPI api = new RestAPI(PLIVO_AUTH_ID, PLIVO_AUTH_TOKEN, "v1");
        
        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("src", fromPhoneNumber); 
        parameters.put("dst", toPhoneNumber);
        parameters.put("text", body);
        
//        parameters.put("url", MY_LISTS_SMS_STATUS_RESPONSE_URL);
//        parameters.put("method", MY_LISTS_SMS_STATUS_RESPONSE_METHOD);
            
        MessageResponse msgResponse = null;
        		
        try {
            // Send the message
            msgResponse = api.sendMessage(parameters);
            // Print the response
            System.out.print(PlivoUtils.getFields(msgResponse));
        } catch (PlivoException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (IllegalAccessException iae) {
        	if(msgResponse == null) {
        		System.err.println("No message response!");
        	} else {
	        	System.err.println("Error extracting fields from response, printing what I can:");
	        	
	            // Print the Api ID
	            System.err.println("Api ID : " + msgResponse.apiId);
	            // Print the Response Message
	            System.err.println("Message : " + msgResponse.message);
	            
	            if (msgResponse.serverCode == 202) {
	                // Print the Message UUID
	                System.err.println("Message UUID : " + msgResponse.messageUuids.get(0).toString());
	            } else {
	                System.err.println(msgResponse.error); 
	            }
        	}
        }
    }
}