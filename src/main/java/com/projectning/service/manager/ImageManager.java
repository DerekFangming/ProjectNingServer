package com.projectning.service.manager;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.projectning.service.exceptions.NotFoundException;

public interface ImageManager {
	
	/**
	 * Save an image as bitmap in local disk and record the image path in database
	 * @param base64 the encoded image file
	 * @param type the type of the image for the project
	 * @param OwnerId the owner of the image
	 * @throws FileNotFoundException if the output file path is not available
	 * @throws IOException if write image file process error
	 */
	public void saveImage(String base64, String type, int ownerId) throws FileNotFoundException, IOException;

	/**
	 * Retrieve image by id
	 * @param imageId the id of the image
	 * @param ownerId the id of the owner
	 * @return base 64 encoded string of the image
	 * @throws NotFoundException if the file is not found
	 */
	public String retrieveImage(int imageId, int ownerId) throws NotFoundException, FileNotFoundException, IOException;
	
	/**
	 * Soft delete (disable) a image base on the id
	 * @param imageId the id of the image. Also the name of the image
	 * @throws NotFoundException if the image is not found
	 * @throws IllegalStateException if the user doesn't have authority to delete the image
	 */
	public void softDeleteImage(int imageId, int ownerId) throws NotFoundException, IllegalStateException;
	
}
