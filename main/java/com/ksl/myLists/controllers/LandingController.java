package com.ksl.myLists.controllers;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ksl.myLists.entities.User;
import com.ksl.myLists.exceptions.ajax.DuplicateUserException;
import com.ksl.myLists.services.UserService;

@Controller
public class LandingController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
//	
//	@Autowired
//	private RememberMeServices rememberMeService;
	
//	@RequestMapping(value = "/landing/login", method = RequestMethod.POST)
//	public void login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		
//		// Do not attempt login if already authenticated as an exception would be thrown
//		if(request.getUserPrincipal() != null) {
//			return;
//		}
//		
//		try {
//			request.login(user.getEmailAddress(), user.getPassword());
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			rememberMeService.loginSuccess(request, response, authentication);
//		} catch(ServletException e) {
//			e.printStackTrace(System.err);
//			throw new BadCredentialsException("The email address and/or password provided are invalid");
//		}
//	}
	 
	@RequestMapping(value = "/landing/accountActivationCode", method = RequestMethod.POST)
	@ResponseBody
	public String getAccountActivationCode(@RequestBody User newUser, HttpServletResponse response) throws IOException {
		String encodedPassword = passwordEncoder.encode(newUser.getPassword());
		newUser.setPassword(encodedPassword);
		
		newUser.setDateCreated(new Date());
		
		// At this point, we are creating a new user, and they need an activation code.
		// Once this is set, the account is active but locked until this code is supplied by their
		// sms phone number
		String activationCode = userService.generateActivationCode();
		newUser.setActivationCode(activationCode);
		
		try {
			userService.saveOrUpdate(newUser);
		} catch (DuplicateUserException due) {
			response.sendError(HttpStatus.SC_NOT_ACCEPTABLE, due.getMessage());
		}
		
		// TODO: Notify email address of activation and provide link if email address does not own phone number.
		
		return activationCode;
	}
}
