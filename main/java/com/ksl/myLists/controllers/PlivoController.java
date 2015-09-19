package com.ksl.myLists.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ksl.myLists.services.external.PlivoSMSService;

@Controller
public class PlivoController {

	@RequestMapping(value = "/plivo/sms/single", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void sendPlivoSMSToSingleReceiver(String from, String to, String message) {
		PlivoSMSService.sendSingleSMSToOnReceiver(from, to, message);
	}
}
