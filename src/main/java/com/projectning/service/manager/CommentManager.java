package com.projectning.service.manager;

import java.util.List;

import com.projectning.service.domain.Comment;
import com.projectning.service.exceptions.NotFoundException;

public interface CommentManager {
	
	/**
	 * Create a comment and save into db
	 * @param body the comment body of this
	 * @param type the type of this comment
	 * @param typeMappingId the mapping Id for the comment type 
	 * @param ownerId the user id of the owner of this comment
	 * @return the id of this comment
	 */
	public int saveComment(String body, String type, int typeMappingId, int ownerId);
	
	/**
	 * Get comment object from comment id
	 * @param commentId the DB ID of a comment
	 * @return a comment object
	 * @throws NotFoundException if the comment does not exist
	 */
	public Comment getCommentById(int commentId) throws NotFoundException;
	
	/**
	 * Soft delete a comment
	 * @param commentId the Id of the comment object
	 * @param ownserId the owner id of the comment
	 * @throws NotFoundException if the comment does not exist
	 * @throws IllegalStateException if the user is not the owner of the image
	 */
	public void softDeleteComment(int commentId, int ownerId) throws NotFoundException, IllegalStateException;
	
	/**
	 * Get a list of comment IDs by comment type and owner id
	 * @param type the type of the comment
	 * @param ownerId the owner(user) id
	 * @return a list of comment IDs that belongs to the user with the specific type
	 * @throws NotFoundException when no id is found
	 */
	public List<Integer> getCommentIdListByType(String type, int ownerId) throws NotFoundException;
	
	/**
	 * Get a list of IDs by comment type, owner id and the mapping id for the comment type
	 * @param type the type of the comment
	 * @param mappingId the mapping id for the comment type
	 * @param ownerId the owner(user) id
	 * @return a list of comment IDs that belongs to the user with the specific type and type mapping ID
	 * @throws NotFoundException when no ID is found
	 */
	public List<Integer> getCommentIdListByTypeAndMappingId(String type, int mappingId, int ownerId) throws NotFoundException;
	
	/**
	 * Get a list of IDs by comment type and type mapping ID, ordered by date.
	 * Only comments owned by friends will be retrieved
	 * @param type the type of the comment
	 * @param mappingId the mapping id for the comment type
	 * @param ownerId the owner(user) id
	 * @return a list of comment IDs that belongs to the user with the specific type and type mapping ID
	 * @throws NotFoundException when no ID is found
	 */
	public List<Integer> getRecentCommentFromFriends(String type, int mappingId, int ownerId) throws NotFoundException;

}
