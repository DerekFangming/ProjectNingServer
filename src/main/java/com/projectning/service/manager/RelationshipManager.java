package com.projectning.service.manager;

import java.util.List;
import java.util.Map;

import com.projectning.service.exceptions.NotFoundException;

public interface RelationshipManager {
	
	/**
	 * Send a friend request to receiver. If the receiver had sent a friend request to the sender, accepts that request.
	 * Both user ids must be pre-checked
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @return a string showing the result of the friend request
	 * @throws IllegalStateException if there are any relationship status error
	 */
	public String sendFriendRequest(int senderId, int receiverId) throws IllegalStateException;

	/**
	 * Accept a friend request from sender.
	 * Both user ids must be pre-checked
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws IllegalStateException if there are any relationship status error
	 * @throws NotFoundException if the friend request does not exist
	 */
	@Deprecated
	public void acceptFriendRequest(int senderId, int receiverId) throws IllegalStateException;
	
	/**
	 * Remove an accepted friend request
	 * Both user ids must be pre-checked
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws NotFoundException if the friend request does not exist
	 */
	public void removeFriend (int senderId, int receiverId) throws NotFoundException;
	
	/**
	 * Create deny relationship with another user. This relationship is confirmed when created.
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws IllegalStateException if you have already denied the user
	 */
	public void denyUser(int senderId, int receiverId) throws IllegalStateException;
	
	
	/**
	 * Find the next user that is not friend nor denied user
	 * @param userId the user
	 * @return the db id of the next user
	 * @throws NotFoundException if there is no user left in the available list
	 */
	public int findNextUser(int userId) throws NotFoundException;
	
	/**
	 * Retrieve the list of friend ID for the given user
	 * Return empty list if no friend is found.
	 * @param userId the user to search for friend list
	 * @return a list of IDs of the given user
	 */
	public List<Integer> getFriendIDList(int userId);
	
	/**
	 * Retrieve a List of friend maps with displayed name and id
	 * Return empty list if no friend is found
	 * Here is the structure of a sample list
	 * [{"displayedName" : "Alen", "id" : 3},
	 * {"displayedName" : "Alex", "id" : 31},
	 * {"displayedName" : "Amy", "id" : 68},
	 * {"displayedName" : "Bob", "id" : 14},
	 * {"displayedName" : "Tommy", "id" : 97},
	 *  ...
	 * ]
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getSortedFriendList(int userId);
	
}
