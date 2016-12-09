package com.projectning.service.manager;

import com.projectning.service.domain.Moment;
import com.projectning.service.exceptions.NotFoundException;

public interface MomentManager {
	
	/**
	 * Create a moment and save into db
	 * @param body the moment body of this 
	 * @param ownerId the user id of the owner of this moment
	 * @return the id of this moment
	 */
	public int saveMoment(String body, int ownerId);
	
	/**
	 * Remove an accepted friend request
	 * Both user ids must be pre-checked
	 * @param senderId the id of the requester
	 * @param receiverId the id of the receiver
	 * @throws NotFoundException if the friend request does not exist
	 */
	public Moment getMoment (int senderId, int receiverId) throws NotFoundException;
	
	
	
}
