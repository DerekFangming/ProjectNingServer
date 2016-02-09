package com.projectning.service.manager;

import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

public interface HelperManager {

	/**
	 * Translate json string to json object
	 * @param jsonStr input json string
	 * @return parsed json object
	 * @throws JsonParsingException if the string is not in json format. This should not happen.
	 */
	public JsonObject stringToJsonHelper(String jsonStr) throws JsonParsingException;
	
	/**
	 * Send email confirmation after the registration
	 * @param to email recipient 
	 * @param code the auth token code
	 */
	public void emailConfirm(String to, String code);
	
	/**
	 * Send email from an email address to recipient
	 * @param from from email address, should be no-replay@fmning.com
	 * @param to recipient email address
	 * @param subject email subject
	 * @param content email body
	 */
	public void sendEmail(String from, String to, String subject, String content);
	
	/**
	 * Generate JWT auth token for email confirmation
	 * @param username the user that the code is generated for
	 * @return JWT code
	 */
	public String getEmailConfirmCode(String username);
}
