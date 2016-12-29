package com.projectning.service.manager;

import java.time.Instant;
import java.util.List;

import com.projectning.service.domain.Feed;
import com.projectning.service.exceptions.NotFoundException;

public interface FeedManager {
	
	/**
	 * Create a feed and save into db
	 * @param body the feed body of this 
	 * @param ownerId the user id of the owner of this feed
	 * @return the id of this feed
	 */
	public int saveFeed(String body, int ownerId);
	
	/**
	 * Get feed object from feed id
	 * @param feedId the DB ID of a feed
	 * @return a feed object
	 * @throws NotFoundException if the feed does not exist
	 */
	public Feed getFeedById(int feedId) throws NotFoundException;
	
	/**
	 * Soft delete a feed
	 * @param feedId the Id of the feed object
	 * @param ownserId the owner id of the feed
	 * @throws NotFoundException if the feed does not exist
	 * @throws IllegalStateException if the user is not the owner of the image
	 */
	public void softDeleteFeed(int feedId, int ownerId) throws NotFoundException, IllegalStateException;
	
	/**
	 * Get a list of most recent feed by the provided date. The maximum number of feed returned
	 * is smaller or equal to the limit
	 * @param ownerId the owner of the feed
	 * @param date the date for the most recent line
	 * @param limit the maximum of feed that will be returned at once
	 * @return a list of feed that meet the criteria
	 * @throws NotFoundException if no feed meets the criteria
	 */
	public List<Feed> getRecentFeedByDate (int ownerId, Instant date, int limit) throws NotFoundException;
	
	/**
	 * Get feed preview image Ids. Will return 4 of them maximumly.
	 * Ids are ordered by time.
	 * @param ownerId the user that the feed images come from
	 * @return a list of ids. If nothing, return empty list
	 */
	public List<Integer> getFeedPreviewImageIdList(int ownerId);
	
}
