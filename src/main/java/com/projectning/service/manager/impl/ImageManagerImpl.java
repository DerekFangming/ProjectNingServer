package com.projectning.service.manager.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.domain.Image;
import com.projectning.service.manager.ImageManager;

public class ImageManagerImpl implements ImageManager{

	@Autowired private ImageDao imageDao;
	
	@Override
	public void saveImage(String base64, String type, int ownerId) throws FileNotFoundException, IOException {
		Image image = new Image();
		image.setLocation("temp");
		image.setType(type);
		image.setCreatedAt(Instant.now());
		image.setOwnerId(ownerId);
		
		imageDao.persist(image);
		System.out.println(image.getId());
		byte[] data = Base64.decodeBase64("");
		//try (OutputStream stream = new FileOutputStream("c:/decode/abc.bmp")) {
		//    stream.write(data);
		//}
	}

}
