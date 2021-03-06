package com.projectning.service.manager;

import java.time.Instant;
import java.util.Map;

import com.projectning.service.exceptions.SessionExpiredException;

public interface HelperManager {
	
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

	/**
	 * Decode string formed JWT to a Java Map object containing all the key value pairs
	 * @param JWTStr the string formed JWT
	 * @return decoded Hashmap with key value pairs
	 */
	public Map<String, Object> decodeJWT(String JWTStr) throws IllegalStateException;
	
	/**
	 * The respond page for the email confirmation
	 * @param msg message string for email confirmation
	 * @return string form of the confirm web page
	 */
	public String getEmailConfirmedPage(String msg);
	
	/**
	 * Create access token base on the username
	 * @param username the user that the access token is generated for
	 * @param expDate the expiration date when this access token becomes invalid
	 * @return string formed JWT encrypted access token
	 */
	public String createAccessToken(String username, Instant expDate);
	
	/**
	 * Check if the expire time in the accessToken has expired.
	 * @param time a string format time
	 * @throws SessionExpiredException if the session has expired
	 */
	public void checkSessionTimeOut(String time) throws SessionExpiredException;
	
}
