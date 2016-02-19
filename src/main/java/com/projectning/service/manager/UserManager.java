package com.projectning.service.manager;

import com.projectning.service.exceptions.NotFoundException;

public interface UserManager {
	
	/**
	 * Create user in database and generate a random salt for the user
	 * Password was set to a temporary one in this method
	 * Username is checked to be not existed, looks like email address, and less than 32 digits
	 * @param username the username
	 * @param offset the time zone offset from GMT
	 * @return 32 digit salt 
	 * @throws IllegalStateException if any of the username check fails
	 */
	public String registerForSalt(String username, int offset) throws IllegalStateException ;
	
	/**
	 * Update the password that client sends. Do some check on the username and password
	 * @param username the resisted username
	 * @param password the password hashed by MD5
	 * @throws IllegalStateException if username and password check fails
	 * @throws NotFoundException if the username doen't exist
	 */
	public void register(String username, String password) throws IllegalStateException, NotFoundException;
	
	/**
	 * Check if the username has been registered
	 * @param username the username to be checked
	 * @return true if the username has been registered
	 */
	public boolean checkUsername(String username);
	
	/**
	 * Update the verification code for the specific user for future check
	 * @param username the user to be updated the verification code
	 * @param code the verification code
	 * @throws NotFoundException if the user is not found
	 */
	public void updateVeriCode(String username, String code) throws NotFoundException;
	
	/**
	 * Check if the verification code matches what exists on the user
	 * @param username the user to be checked
	 * @param code the verification code
	 * @throws NotFoundException if the user with the verification code does not exist
	 */
	public void checkVeriCode(String username, String code) throws NotFoundException;
	
	/**
	 * Confirm that the email address of the user has been confirmed
	 * @param username the user name to set email flag to be true
	 * @throws NotFoundException if the user is not found
	 */
	public void confirmEmail(String username) throws NotFoundException;
	
	/**
	 * Update access token for a given user
	 * @param username the user
	 * @param token the access token code
	 * @throws NotFoundException id the user is not found by its username
	 */
	public void updateAccessToken(String username, String token) throws NotFoundException;
	
	/**
	 * Return salt string of a given username for login purpose
	 * @param username the user
	 * @return the salt string of that user
	 * @throws NotFoundException if the user name is not found
	 */
	public String loginForSalt(String username) throws NotFoundException;

}
