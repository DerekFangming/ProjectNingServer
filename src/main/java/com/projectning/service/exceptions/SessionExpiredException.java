package com.projectning.service.exceptions;

@SuppressWarnings("serial")
public class SessionExpiredException extends RuntimeException{
	
	public SessionExpiredException(){
		super();
	}
	
	public SessionExpiredException(String msg){
		super(msg);
	}

}
