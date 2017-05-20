package com.projectning.service.manager;

import java.util.Map;

import com.projectning.service.domain.User;
import com.projectning.service.domain.UserDetail;
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
	
	/**
	 * Check if the login username and password is correct
	 * @param username the user
	 * @param password the hashed password
	 * @param accessToken the token string that need to be updated
	 * @throws NotFoundException
	 */
	public User login (String username, String password, String accessToken) throws NotFoundException;

	/**
	 * Get user ID by username
	 * @param username the username of this user
	 * @return the database id of the user
	 * @throws NotFoundException if the user is not found
	 */
	public int getUserId(String username) throws NotFoundException;
	
	/**
	 * Check if the given user id exists in the user table or not
	 * @param id the user id
	 * @throws NotFoundException if the user doesn't exist
	 */
	public void checkUserIdExistance(int id) throws NotFoundException;
	
	/**
	 * Return the username of the user
	 * @param userId the id of the user
	 * @return the username of the user
	 * @throws NotFoundException if the user with the id does not exist
	 */
	public String getUsername(int userId) throws NotFoundException;
	
	/**
	 * Validate the access token. Check if the access token is valid, if it expires.
	 * @param request the request object (JSON object, map)
	 * @return the user id for this access token
	 * @throws NullPointerException if the request does not have access token string
	 * @throws NotFoundException if the user does not exist
	 * @throws IllegalStateException if the access token is invalid
	 */
	public int validateAccessToken(Map<String, Object> request) 
			throws NullPointerException, NotFoundException, IllegalStateException;
	
	/**
	 * Get a name of a user for displaying purpose. This method will try to get nickname from user detail.
	 * If there is no nickname, get real name. If there is no user detail, get the username.
	 * @param userId the id of the user
	 * @return a name in the preference order mentioned above
	 * @throws NotFoundException if the user does not exist
	 */
	public String getUserDisplayedName(int userId) throws NotFoundException;
	
	/**
	 * Change the password of a given user
	 * @param username the username
	 * @param oldPwd original md5 encrypted password
	 * @param newPwd the new md5 encrypted password
	 * @throws IllegalStateException if password format is incorrect
	 * @throws NotFoundException if the user does not exist
	 */
	public void changePassword(String username, String oldPwd, String newPwd, String accessToken) 
			throws IllegalStateException, NotFoundException;
	
	/* The following methods are for user details*/

	/**
	 * Return the user detail object for the given user
	 * @param userId the id of the user
	 * @return the user detail object
	 * @throws NotFoundException if the user does not have details listed
	 */
	public UserDetail getUserDetail(int userId) throws NotFoundException;
	
	/**
	 * Save the detail of a user to database. Update current record if already exist
	 * @param userId
	 * @param name the real name
	 * @param nickname
	 * @param age
	 * @param gender mail is M, female is F
	 * @param location
	 * @param whatsUp
	 */
	public void saveUserDetail(int userId, String name, String nickname, int age, String gender,
			String location, String whatsUp);
	
}
