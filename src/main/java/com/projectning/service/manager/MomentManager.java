package com.projectning.service.manager;

import java.time.Instant;
import java.util.List;

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
	 * Get a list of most recent moment by the provided date. The maximum number of moment returned
	 * is smaller or equal to the limit
	 * @param ownerId the owner of the moment
	 * @param date the date for the most recent line
	 * @param limit the maximum of moment that will be returned at once
	 * @return a list of moment that meet the criteria
	 * @throws NotFoundException if no moment meets the criteria
	 */
	public List<Moment> getRecentMomentByDate (int ownerId, Instant date, int limit) throws NotFoundException;
	
	
	
}
