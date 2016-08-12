package com.projectning.service.manager;

import com.projectning.service.exceptions.NotFoundException;

public interface RelationshipManager {
	
	/**
	 * Send a friend request to receiver
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws NotFoundException if receiver id does not exist in the user table. Sender id is pre-checked
	 */
	public void friendRequest(int senderId, int receiverId) throws NotFoundException;

	
	
}
