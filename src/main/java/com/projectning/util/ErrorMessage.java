package com.projectning.util;

public enum ErrorMessage {
	INCORECT_PARAM("Request parameters incorrect"),
	USER_NOT_FOUND("User does not exist");
	
	private final String msg;
	
	ErrorMessage(String msg) {
        this.msg = msg;
    }
	
	public String getMsg(){
		return msg;
	}

}
