package com.projectning.service.manager.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import com.projectning.util.ErrorMessage;
import com.projectning.util.Util;

@Component
public class ImageManagerImpl implements ImageManager{

	@Autowired private ImageDao imageDao;
	
	@Override
	public void saveImage(String base64, String type, int ownerId, String title) throws FileNotFoundException, IOException {
		Image image = new Image();
		image.setLocation("");
		image.setType(Util.verifyImageType(type));
		image.setCreatedAt(Instant.now());
		image.setOwnerId(ownerId);
		image.setEnabled(true);
		image.setTitle(title);
		
		int id = imageDao.persist(image);
		NVPair pair = new NVPair(ImageDao.Field.LOCATION.name, Util.imagePath + Integer.toString(id) + ".jpg");
		imageDao.update(id, pair);
		
		if(base64.contains(","))
			base64 = base64.split(",")[1];
		
		byte[] data = Base64.decodeBase64(base64);
		try (OutputStream stream = new FileOutputStream(Util.imagePath + Integer.toString(id) + ".jpg")) {
		    stream.write(data);
		}
	}

	@Override
	public void softDeleteImage(int imageId, int ownerId) throws NotFoundException, IllegalStateException {
		
		QueryTerm value = ImageDao.Field.ID.getQueryTerm(imageId);
		Image image;
		try{
			image = imageDao.findObject(value);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.NO_IMAGE_TO_DELETE.getMsg());
		}
		
		if(image.getOwnerId() != ownerId)
			throw new IllegalStateException(ErrorMessage.UNAUTHORIZED_IMAGE_DELETE.getMsg());
		
		NVPair pair = new NVPair(ImageDao.Field.ENABLED.name, false);
		imageDao.update(image.getId(), pair);
	}

	@Override
	public Image retrieveImageById(int imageId) throws NotFoundException, FileNotFoundException, IOException{
		
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(ImageDao.Field.ID.getQueryTerm(imageId));
		values.add(ImageDao.Field.ENABLED.getQueryTerm(true));
		Image img;
		try{
			img= imageDao.findObject(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.IMAGE_NOT_FOUND.getMsg());
		}
		
		File originalFile = new File(img.getLocation());
		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
        byte[] bytes = new byte[(int)originalFile.length()];
        fileInputStreamReader.read(bytes);
        encodedBase64 = new String(Base64.encodeBase64(bytes));
        fileInputStreamReader.close();
        
        img.setImageData(encodedBase64);
        return img;
	}

	@Override
	public List<Integer> getImageIdListByType(String type, int ownerId) throws NotFoundException{
		return getImageIdListByTypeAndMappingId(type, -1, ownerId);
	}
	
	@Override
	public List<Integer> getImageIdListByTypeAndMappingId(String type, int mappingId, int ownerId) throws NotFoundException{
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(ImageDao.Field.TYPE.getQueryTerm(type));
		values.add(ImageDao.Field.OWNER_ID.getQueryTerm(ownerId));
		values.add(ImageDao.Field.ENABLED.getQueryTerm(true));
		if(mappingId != -1)
			values.add(ImageDao.Field.TYPE_MAPPING_ID.getQueryTerm(mappingId));
		
		try{
			return imageDao.findAllIds(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.IMAGE_NOT_FOUND.getMsg());
		}
	}

	@Override
	public int getSingltonImageIdByType(String type, int ownerId) throws NotFoundException, IllegalStateException {
		List<Integer> idList = getImageIdListByType(type, ownerId);
		if(idList.size() != 1){
			throw new IllegalStateException(ErrorMessage.INTERNAL_LOGIC_ERROR.getMsg());
		}
		return idList.get(0);
	}
	
	//Image processing helpers
	private BufferedImage cropImageToSquare(BufferedImage img){
		int width = img.getWidth();
		int height = img.getHeight();
		int cropStart;
		if(width > height){
			cropStart = (width - height) / 2;
			img = img.getSubimage(cropStart, 0, height, height);
		}else if (width < height){
			cropStart = (height - width) / 2;
			img = img.getSubimage(0, cropStart, width, width);
		}
		return img;
	}
	
	private BufferedImage resize(BufferedImage img, int width, int height) { 
	    java.awt.Image tmp = img.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
	    BufferedImage newImg = new BufferedImage(width, height, img.getType());

	    Graphics2D g2d = newImg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return newImg;
	}
	
}
