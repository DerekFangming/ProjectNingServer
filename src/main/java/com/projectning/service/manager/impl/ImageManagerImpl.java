package com.projectning.service.manager.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.domain.Image;
import com.projectning.service.manager.ImageManager;

@Component
public class ImageManagerImpl implements ImageManager{

	@Autowired private ImageDao imageDao;
	
	@Override
	public void saveImage(String base64, String type, int ownerId) throws FileNotFoundException, IOException {
		Image image = new Image();
		image.setLocation("temp");
		image.setType(type);
		image.setCreatedAt(Instant.now());
		image.setOwnerId(ownerId);
		
		int id = (int) imageDao.persist(image);
		NVPair pair = new NVPair(ImageDao.Field.LOCATION.name, "/Volumes/Data/images/" + Integer.toString(id) + ".bmp");
		imageDao.update(id, pair);
		
		if(base64.contains(","))
			base64 = base64.split(",")[1];
		
		byte[] data = Base64.decodeBase64(base64);
		try (OutputStream stream = new FileOutputStream("/Volumes/Data/images/" + Integer.toString(id) + ".bmp")) {
		    stream.write(data);
		}
	}

}
