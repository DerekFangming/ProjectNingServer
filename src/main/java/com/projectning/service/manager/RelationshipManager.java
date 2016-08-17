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
	public void acceptFriendRequest(int senderId, int receiverId) throws IllegalStateException, NotFoundException;
	
	/**
	 * Remove an accepted friend request
	 * Both user ids must be pre-checked
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws IllegalStateException if there are any relationship status error
	 * @throws NotFoundException if the friend request does not exist
	 */
	public void removeFriend (int senderId, int receiverId) throws IllegalStateException, NotFoundException;
	
}
