package com.projectning.util;

public enum ErrorMessage {
	INCORECT_PARAM("Request parameters incorrect"),
	USER_NOT_FOUND("User does not exist"),
	//Image related
	IMAGE_NOT_FOUND("Image does not exist"),
	INCORRECT_INTER_IMG_PATH("");
	
	private final String msg;
	
	ErrorMessage(String msg) {
        this.msg = msg;
    }
	
	public String getMsg(){
		return msg;
	}

}
