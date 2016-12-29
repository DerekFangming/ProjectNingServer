package com.projectning.service.manager;

import java.time.Instant;
import java.util.List;

import com.projectning.service.domain.Feed;
import com.projectning.service.exceptions.NotFoundException;

public interface FeedManager {
	
	/**
	 * Create a moment and save into db
	 * @param body the moment body of this 
	 * @param ownerId the user id of the owner of this moment
	 * @return the id of this moment
	 */
	public int saveMoment(String body, int ownerId);
	
	/**
	 * Get moment object from moment id
	 * @param momentId the DB ID of a moment
	 * @return a moment object
	 * @throws NotFoundException if the moment does not exist
	 */
	public Feed getMomentById(int momentId) throws NotFoundException;
	
	/**
	 * Soft delete a moment
	 * @param momentId the Id of the moment object
	 * @param ownserId the owner id of the moment
	 * @throws NotFoundException if the moment does not exist
	 * @throws IllegalStateException if the user is not the owner of the image
	 */
	public void softDeleteMoment(int momentId, int ownerId) throws NotFoundException, IllegalStateException;
	
	/**
	 * Get a list of most recent moment by the provided date. The maximum number of moment returned
	 * is smaller or equal to the limit
	 * @param ownerId the owner of the moment
	 * @param date the date for the most recent line
	 * @param limit the maximum of moment that will be returned at once
	 * @return a list of moment that meet the criteria
	 * @throws NotFoundException if no moment meets the criteria
	 */
	public List<Feed> getRecentMomentByDate (int ownerId, Instant date, int limit) throws NotFoundException;
	
	/**
	 * Get moment preview image Ids. Will return 4 of them maximumly.
	 * Ids are ordered by time.
	 * @param ownerId the user that the moment images come from
	 * @return a list of ids. If nothing, return empty list
	 */
	public List<Integer> getMomentPreviewImageIdList(int ownerId);
	
}
