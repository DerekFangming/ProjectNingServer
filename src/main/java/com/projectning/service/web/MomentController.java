package com.projectning.service.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.domain.Moment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.ImageManager;
import com.projectning.service.manager.MomentManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ImageType;
import com.projectning.util.Util;

@Controller
public class MomentController {
	
	@Autowired private UserManager userManager;
	@Autowired private MomentManager momentManager;
	@Autowired private ImageManager imageManager;
	
	@RequestMapping("/get_recent_moments")
    public ResponseEntity<Map<String, Object>> getRecentMoments(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			int userId = (int)request.get("userId");
			int limit = 10;
			try{
				limit = (int)request.get("limit");
			}catch(NullPointerException e){
				//
			}
			Instant checkPoint = Instant.now();
			try{
				String timeStr = (String)request.get("checkPoint");
				checkPoint = Instant.parse(timeStr);
			}catch(NullPointerException e){
				//
			}
			List<Moment> momentList = momentManager.getRecentMomentByDate(userId, checkPoint, limit);
			List<Map<String, Object>> processedMomentList = new ArrayList<Map<String, Object>>();
			
			for(Moment m : momentList){
				Map<String, Object> processedMoment = new HashMap<String, Object>();
				processedMoment.put("momentId", m.getId());
				processedMoment.put("momentBody", m.getBody());
				processedMoment.put("createdAt", m.getCreatedAt().toString());
				try{
					imageManager.getImageIdListByTypeAndMappingId(ImageType.MOMENT.getName(), m.getId(), userId);
					processedMoment.put("hasImage", true);
				}catch(NotFoundException e){
					processedMoment.put("hasImage", false);
				}
				
				processedMomentList.add(processedMoment);
			}
			respond.put("momentList", processedMomentList);
			respond.put("checkPoint", momentList.get(momentList.size() - 1).getCreatedAt().toString());
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/create_moment_cover_image")
    public ResponseEntity<Map<String, Object>> getMomentCoverImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		
		try{
			int id = userManager.validateAccessToken(request);
			int momentId = (int)request.get("momentId");
			List<Integer> imgIdList = imageManager.getImageIdListByTypeAndMappingId(ImageType.MOMENT.getName(), momentId, id);
			int imgCount = imgIdList.size();
			BufferedImage newImg;
			
			if(imgCount == 1){
				File imgFile = new File(imageManager.retrieveImageById(imgIdList.get(0)).getLocation());
				newImg = ImageIO.read(imgFile);
				newImg = cropImageToSquare(newImg);
				newImg = resize(newImg, 60, 60);
			}else if (imgCount == 2){
				File imgFileLeft = new File(imageManager.retrieveImageById(imgIdList.get(0)).getLocation());
				File imgFileRight = new File(imageManager.retrieveImageById(imgIdList.get(1)).getLocation());
				BufferedImage imgLeft = ImageIO.read(imgFileLeft);
				BufferedImage imgRight = ImageIO.read(imgFileRight);
				imgLeft = cropImageToSquare(imgLeft);
				imgRight = cropImageToSquare(imgRight);
				imgLeft = resize(imgLeft, 60, 60);
				imgRight = resize(imgRight, 60, 60);
				imgLeft = imgLeft.getSubimage(15, 0, 29, 60);
				imgRight = imgRight.getSubimage(15, 0, 29, 60);
				
				newImg = new BufferedImage(60, 60,  imgLeft.getType());
				Graphics2D g = (Graphics2D) newImg.getGraphics();
				g.setBackground(Color.WHITE);
				g.clearRect(0, 0, 60, 60);
			    g.drawImage(imgLeft, 0, 0, null);
			    g.drawImage(imgRight, 31, 0, null);
			    g.dispose();
			}else if (imgCount == 3){
				File imgFileLeft = new File(imageManager.retrieveImageById(imgIdList.get(0)).getLocation());
				File imgFileRightTop = new File(imageManager.retrieveImageById(imgIdList.get(1)).getLocation());
				File imgFileRightBot = new File(imageManager.retrieveImageById(imgIdList.get(2)).getLocation());
				BufferedImage imgLeft = ImageIO.read(imgFileLeft);
				BufferedImage imgRightTop = ImageIO.read(imgFileRightTop);
				BufferedImage imgRightBot = ImageIO.read(imgFileRightBot);
				imgLeft = cropImageToSquare(imgLeft);
				imgRightTop = cropImageToSquare(imgRightTop);
				imgRightBot = cropImageToSquare(imgRightBot);
				imgLeft = resize(imgLeft, 60, 60);
				imgRightTop = resize(imgRightTop, 29, 29);
				imgRightBot = resize(imgRightBot, 29, 29);
				imgLeft = imgLeft.getSubimage(15, 0, 29, 60);
				
				newImg = new BufferedImage(60, 60,  imgLeft.getType());
				Graphics2D g = (Graphics2D) newImg.getGraphics();
				g.setBackground(Color.WHITE);
				g.clearRect(0, 0, 60, 60);
			    g.drawImage(imgLeft, 0, 0, null);
			    g.drawImage(imgRightTop, 31, 0, null);
			    g.drawImage(imgRightBot, 31, 31, null);
			    g.dispose();
			}else{
				File imgFileLeftTop = new File(imageManager.retrieveImageById(imgIdList.get(0)).getLocation());
				File imgFileLeftBot = new File(imageManager.retrieveImageById(imgIdList.get(1)).getLocation());
				File imgFileRightTop = new File(imageManager.retrieveImageById(imgIdList.get(2)).getLocation());
				File imgFileRightBot = new File(imageManager.retrieveImageById(imgIdList.get(3)).getLocation());
				BufferedImage imgLeftTop = ImageIO.read(imgFileLeftTop);
				BufferedImage imgLeftBot = ImageIO.read(imgFileLeftBot);
				BufferedImage imgRightTop = ImageIO.read(imgFileRightTop);
				BufferedImage imgRightBot = ImageIO.read(imgFileRightBot);
				imgLeftTop = cropImageToSquare(imgLeftTop);
				imgLeftBot = cropImageToSquare(imgLeftBot);
				imgRightTop = cropImageToSquare(imgRightTop);
				imgRightBot = cropImageToSquare(imgRightBot);
				imgLeftTop = resize(imgLeftTop, 29, 29);
				imgLeftBot = resize(imgLeftBot, 29, 29);
				imgRightTop = resize(imgRightTop, 29, 29);
				imgRightBot = resize(imgRightBot, 29, 29);
				
				newImg = new BufferedImage(60, 60,  imgLeftTop.getType());
				Graphics2D g = (Graphics2D) newImg.getGraphics();
				g.setBackground(Color.WHITE);
				g.clearRect(0, 0, 60, 60);
			    g.drawImage(imgLeftTop, 0, 0, null);
			    g.drawImage(imgLeftBot, 0, 31, null);
			    g.drawImage(imgRightTop, 31, 0, null);
			    g.drawImage(imgRightBot, 31, 31, null);
			    g.dispose();
			}
			imageManager.saveImage(newImg, ImageType.MOMENT_COVER.getName(), momentId, id, "");
			
			respond.put("error","");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
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
	    Image tmp = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    BufferedImage newImg = new BufferedImage(width, height, img.getType());

	    Graphics2D g = newImg.createGraphics();
	    g.drawImage(tmp, 0, 0, null);
	    g.dispose();

	    return newImg;
	} 

}
