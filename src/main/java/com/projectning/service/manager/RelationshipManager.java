package com.projectning.service.manager;

import com.projectning.service.exceptions.NotFoundException;

public interface RelationshipManager {
	
	/**
	 * Send a friend request to receiver. If the receiver had sent a friend request to the sender, accepts that request.
	 * Both user ids must be pre-checked
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws IllegalStateException if there are any relationship status error
	 */
	public void sendFriendRequest(int senderId, int receiverId) throws IllegalStateException;

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
	 * @throws NotFoundException if there is no user left in the available list
	 */
	public void findNextUser(int userId) throws NotFoundException;
	
}
