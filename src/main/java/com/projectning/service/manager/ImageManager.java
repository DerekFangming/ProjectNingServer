package com.projectning.service.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.projectning.service.domain.Image;
import com.projectning.service.exceptions.NotFoundException;

public interface ImageManager {
	
	/**
	 * Save an image as bitmap in local disk and record the image path in database
	 * @param base64 the encoded image file
	 * @param type the type of the image for the project. Defaulted to Others if type is not recognized
	 * @param ownerId the owner of the image
	 * @param title the title of the image, if applicable. Defaulted to empty String if type is not recognized
	 * @throws FileNotFoundException if the output file path is not available
	 * @throws IOException if write image file process error
	 */
	public void saveImage(String base64, String type, int ownerId, String title) throws FileNotFoundException, IOException;

	/**
	 * Retrieve image by id
	 * @param imageId the id of the image
	 * @return Image image object
	 * @throws NotFoundException if the file is not found
	 */
	public Image retrieveImageById(int imageId) throws NotFoundException, FileNotFoundException, IOException;
	
	/**
	 * Soft delete (disable) a image base on the id
	 * @param imageId the id of the image. Also the name of the image
	 * @throws NotFoundException if the image is not found
	 * @throws IllegalStateException if the user doesn't have authority to delete the image
	 */
	public void softDeleteImage(int imageId, int ownerId) throws NotFoundException, IllegalStateException;
	
	/**
	 * Get a list of image IDs by image type
	 * Return an empty list if no image found
	 * @param type the type of the image
	 * @param ownerId the owner(user) id
	 * @return a list of IDs that belongs the user and type
	 * @throws NotFoundException when no id is found
	 */
	public List<Integer> getImageIdListByType(String type, int ownerId) throws NotFoundException;
	
	
}
