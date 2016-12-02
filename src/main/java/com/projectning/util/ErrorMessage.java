package com.projectning.util;

public enum ErrorMessage {
	//General
	UNKNOWN_ERROR("Unknown error."),
	//Security related
	INVALID_ACCESS_TOKEN("The access token you are using is not valid. Please login again."),
	INVALID_VERIFICATION_CODE("The verification code does not match what's on the record."),
	//HTTP related
	INCORRECT_PARAM("Request parameters incorrect."),
	//User related
	USERNAME_UNAVAILABLE("The username is already taken. Please try a different one."),
	USERNAME_TOO_LONG("Username cannot be logger than 32 characters."),
	USERNAME_NOT_EMAIL("Username must be an email."),
	USER_NOT_FOUND("The user does not exist."),
	SESSION_EXPIRED("Session expired. Please login again."),
	USER_INTERN_ERROR("Internal error. Please only use methods provided in SDK."),
	LOGIN_FAIL("The username or password you entered is not correct. Please try again."),
	INTERNAL_LOGIC_ERROR("Internal user error. Please report to admin."),
	//User detail related
	USER_DETAIL_NOT_FOUND("The user does not have detail information."),
	//Image related
	IMAGE_NOT_FOUND("The image you are looking for does not exist."),
	IMAGE_TYPE_NOT_FOUND("No image exists with the given type."),
	NO_IMAGE_TO_DELETE("The image you are trying to delete does not exist."),
	INCORRECT_INTER_IMG_PATH("Internal error, image path not found."),
	INCORRECT_INTER_IMG_IO("Internal error, cannot write image file."),
	UNAUTHORIZED_DELETE("Cannot delete an image that is not yours."),
	AVATAR_NOT_FOUND("The user does not have an avatar."),
	//Relationship related
	NOT_FRIEND("You are not a friend of him/her."),
	ALREADY_REQUESTED("You have already sent a request to him/her."),
	ALREADY_FRIEND("You are already friend of him/her."),
	ALREADY_DENIED("You have already denied him/her."),
	NO_MORE_USER("There are no more users. Please try again later."),
	//Test case related
	SHOULD_NOT_PASS_ERROR("This method should fail, but passed.");
	
	private final String msg;
	
	ErrorMessage(String msg) {
        this.msg = msg;
    }
	
	public String getMsg(){
		return msg;
	}

}
