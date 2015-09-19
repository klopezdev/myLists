package com.ksl.myLists.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class AjaxResponse {

	private int statusCode;
	
	private String message;
	
}
