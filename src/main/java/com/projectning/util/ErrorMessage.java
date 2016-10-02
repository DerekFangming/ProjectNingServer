package com.projectning.util;

public enum ErrorMessage {
	//JWT related
	INCORRECT_DATE_FORMAT(""),
	//HTTP related
	INCORRECT_PARAM("Request parameters incorrect."),
	//General
	RESOURCE_NOT_FOUND("The resources do not exist."),
	//User related
	USER_NOT_FOUND("User does not exist."),
	SESSION_EXPIRED("Session expired, please login again."),
	//Image related
	IMAGE_NOT_FOUND("The image you are looking for does not exist."),
	IMAGE_TYPE_NOT_FOUND("No image exists with the given type."),
	NO_IMAGE_TO_DELETE("The image you are trying to delete does not exist."),
	INCORRECT_INTER_IMG_PATH("Internal error, image path not found."),
	INCORRECT_INTER_IMG_IO("Internal error, cannot write image file."),
	UNAUTHORIZED_DELETE("Cannot delete an image that is not yours."),
	AVATAR_NOT_FOUND("The user does not have an avatar."),
	//Relationship related
	ALREADY_REQUESTED("You have already sent a request to him/her."),
	ALREADY_FRIEND("You are already friend of him/her."),
	ALREADY_DENIED("You have already denied him/her.");
	
	private final String msg;
	
	ErrorMessage(String msg) {
        this.msg = msg;
    }
	
	public String getMsg(){
		return msg;
	}

}
