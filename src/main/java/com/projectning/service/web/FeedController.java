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

import com.projectning.service.domain.Feed;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.ImageManager;
import com.projectning.service.manager.FeedManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.ImageType;
import com.projectning.util.Util;

@Controller
public class FeedController {
	
	@Autowired private UserManager userManager;
	@Autowired private FeedManager feedManager;
	@Autowired private ImageManager imageManager;
	
	@RequestMapping("/get_recent_feeds")
    public ResponseEntity<Map<String, Object>> getRecentFeeds(@RequestBody Map<String, Object> request) {
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
			List<Feed> feedList = feedManager.getRecentFeedByDate(userId, checkPoint, limit);
			List<Map<String, Object>> processedFeedList = new ArrayList<Map<String, Object>>();
			
			for(Feed m : feedList){
				Map<String, Object> processedFeed = new HashMap<String, Object>();
				processedFeed.put("feedId", m.getId());
				processedFeed.put("feedBody", m.getBody());
				processedFeed.put("createdAt", m.getCreatedAt().toString());
				try{
					List<Integer> idList = imageManager.getImageIdListByTypeAndMappingId(ImageType.FEED.getName(), 
							m.getId(), userId);
					processedFeed.put("hasImage", true);
					processedFeed.put("imageIdList", idList);
				}catch(NotFoundException e){
					processedFeed.put("hasImage", false);
					processedFeed.put("imageIdList", null);
				}
				
				processedFeedList.add(processedFeed);
			}
			respond.put("feedList", processedFeedList);
			respond.put("checkPoint", feedList.get(feedList.size() - 1).getCreatedAt().toString());
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_feed_preview_images")
	public ResponseEntity<Map<String, Object>> getFeedPreviewImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			int userId = (int)request.get("userId");
			
			List<Integer> idList = feedManager.getFeedPreviewImageIdList(userId);
			
			respond.put("idList", idList);
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_feed_cover_img")
    public ResponseEntity<Map<String, Object>> getFeedCoverImg(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			int feedId = (int)request.get("feedId");
			int userId = (int)request.get("userId");
			List<Integer> imgList = imageManager.getImageIdListByTypeAndMappingId(ImageType.FEED_COVER.getName(), feedId, userId);
			if(imgList.size() != 1)
				throw new IllegalStateException(ErrorMessage.INVALID_FEED_COVER.getMsg());
			
			String imgData = imageManager.retrieveImageById(imgList.get(0)).getImageData();
			
			respond.put("image", imgData);
			respond.put("error", "");
			
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/create_feed_cover_image")
    public ResponseEntity<Map<String, Object>> getFeedCoverImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int userId = userManager.validateAccessToken(request);
			int feedId = (int)request.get("feedId");
			List<Integer> imgIdList = 
					imageManager.getImageIdListByTypeAndMappingId(ImageType.FEED.getName(), feedId, userId);
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
			imageManager.saveImage(newImg, ImageType.FEED_COVER.getName(), feedId, userId, "");
			
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
