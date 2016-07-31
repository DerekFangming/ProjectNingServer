package com.projectning.util;

public enum ErrorMessage {
	//JWT related
	INCORRECT_DATE_FORMAT(""),
	//HTTP related
	INCORRECT_PARAM("Request parameters incorrect"),
	//User related
	USER_NOT_FOUND("User does not exist"),
	SESSION_EXPIRED("Session expired, please login again"),
	//Image related
	IMAGE_NOT_FOUND("Image does not exist"),
	INCORRECT_INTER_IMG_PATH("Internal error, image path not found"),
	INCORRECT_INTER_IMG_IO("Internal error, cannot write image file"),
	UNAUTHORIZED_DELETE("Cannot delete an image that is not yours");
	
	private final String msg;
	
	ErrorMessage(String msg) {
        this.msg = msg;
    }
	
	public String getMsg(){
		return msg;
	}

}
