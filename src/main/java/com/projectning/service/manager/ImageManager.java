package com.projectning.service.manager;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageManager {
	
	public void saveImage(String base64, String type, int OwnerId) throws FileNotFoundException, IOException;

}
