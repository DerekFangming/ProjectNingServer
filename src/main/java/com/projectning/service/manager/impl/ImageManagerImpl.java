package com.projectning.service.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.domain.Image;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.ImageManager;

@Component
public class ImageManagerImpl implements ImageManager{

	@Autowired private ImageDao imageDao;
	
	@Override
	public void saveImage(String base64, String type, int ownerId, String title) throws FileNotFoundException, IOException {
		Image image = new Image();
		image.setLocation("temp");
		image.setType(type);
		image.setCreatedAt(Instant.now());
		image.setOwnerId(ownerId);
		image.setEnabled(true);
		image.setTitle(title);
		
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

	@Override
	public void softDeleteImage(int imageId, int ownerId) throws NotFoundException, IllegalStateException {
		
		QueryTerm value = ImageDao.Field.ID.getQueryTerm(imageId);
		Image image = imageDao.findObject(value);
		
		if(image.getOwnerId() != ownerId)
			throw new IllegalStateException("You cannot delete an image that is not yours.");
		
		NVPair pair = new NVPair(ImageDao.Field.ENABLED.name, false);
		imageDao.update(image.getId(), pair);
	}

	@Override
	public Image retrieveImageById(int imageId, int ownerId) throws NotFoundException, FileNotFoundException,IOException{
		
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(ImageDao.Field.ID.getQueryTerm(imageId));
		values.add(ImageDao.Field.OWNER_ID.getQueryTerm(ownerId));
		Image img = imageDao.findObject(values);
		
		File originalFile = new File(img.getLocation());
		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
        byte[] bytes = new byte[(int)originalFile.length()];
        fileInputStreamReader.read(bytes);
        encodedBase64 = new String(Base64.encodeBase64(bytes));
        fileInputStreamReader.close();
        
        img.setLocation(encodedBase64);
        return img;
	}

}