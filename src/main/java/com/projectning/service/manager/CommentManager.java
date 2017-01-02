package com.projectning.service.manager;

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

}
