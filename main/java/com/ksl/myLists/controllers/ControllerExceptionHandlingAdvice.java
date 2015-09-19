package com.ksl.myLists.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ksl.myLists.exceptions.ajax.AjaxException;
import com.ksl.myLists.exceptions.ajax.DuplicateUserException;

@ControllerAdvice
public class ControllerExceptionHandlingAdvice {
 
	@ExceptionHandler(DuplicateUserException.class)
	@ResponseBody
	public AjaxResponse handleAjaxExceptionResponse(AjaxException ex, HttpServletResponse response) {
		response.setStatus(ex.getStatusCode());
		return new AjaxResponse(ex.getStatusCode(), ex.getMessage());
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public AjaxResponse handleUnauthorizedExceptions(Exception exception) {
		return new AjaxResponse(org.apache.http.HttpStatus.SC_UNAUTHORIZED, exception.getMessage());
	}
}
