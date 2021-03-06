package com.projectning.service.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.domain.Image;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.ImageManager;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.ImageType;
import com.projectning.util.RelationshipType;
import com.projectning.util.Util;

@Controller
public class ImageController {
	
	@Autowired private ImageManager imageManager;
	@Autowired private UserManager userManager;
	@Autowired private RelationshipManager relationshipManager;
	
	@RequestMapping("/upload_image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int id = userManager.validateAccessToken(request);
			
			String title = "";
			try{
				title = (String)request.get("title");
			}catch(NullPointerException e){
				//
			}
			
			String verifiedType = "";
			int typeMappingId = 0;
			try{
				verifiedType = Util.verifyImageType((String)request.get("type"));
				typeMappingId = (int)request.get("typeMappingId");
			}catch(NullPointerException e){
				//
			}
			
			imageManager.saveImage((String)request.get("image"), verifiedType, typeMappingId, id, title);
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/download_image_by_id")
    public ResponseEntity<Map<String, Object>> downloadImageById(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			int imageId = (int)request.get("imageId");
			
			Image image = imageManager.retrieveImageById(imageId);
			
			respond.put("error", "");
			respond.put("createdAt", image.getCreatedAt().toString());
			respond.put("image", image.getImageData());
			respond.put("title", image.getTitle());
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/delete_image")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int id = userManager.validateAccessToken(request);
			
			int imageId = (int)request.get("imageId");
			imageManager.softDeleteImage(imageId, id);
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/get_image_ids_by_type")
	public ResponseEntity<Map<String, Object>> getImageIds(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			
			int userId = (int)request.get("userId");
			
			respond.put("idList", imageManager.getImageIdListByType((String)request.get("type"), userId));
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_singleton_img_by_type")
	public ResponseEntity<Map<String, Object>> getAvatar(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			
			int userId = (Integer)request.get("userId");
			String imgType = (String)request.get("imgType");
			int avatarId;
			try{
				avatarId = imageManager.getSingltonImageIdByType(imgType, userId);
			}catch(NotFoundException nfe){
				throw new NotFoundException(ErrorMessage.SINGLETON_IMG_NOT_FOUND.getMsg() + imgType.toLowerCase());
			}
			
			Image avatar = imageManager.retrieveImageById(avatarId);
			respond.put("image", avatar.getImageData());
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_next_avatar")
	public ResponseEntity<Map<String, Object>> getNextAvatar(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			Thread.sleep(2000);
			int senderId = userManager.validateAccessToken(request);
			
			try{
				int receiverId = (int)request.get("userId");
				String action = (String)request.get("action");
				
				userManager.checkUserIdExistance(receiverId);
				
				if(action.equals("deny")){
					relationshipManager.denyUser(senderId, receiverId);
				}else if(action.equals("friend")){
					String status = relationshipManager.sendFriendRequest(senderId, receiverId);
					if(status.equals(RelationshipType.FRIEND_CONFIRMED.getName())){
						respond.put("status", true);
					}else{
						respond.put("status", false);
					}
				}
			}catch(NullPointerException npe){
				//
			}
			
			int nextUserId = relationshipManager.findNextUser(senderId);
			int avatarId;
			try{
				avatarId = imageManager.getSingltonImageIdByType(ImageType.AVATAR.getName(), nextUserId);
			}catch(NotFoundException nfe){
				throw new NotFoundException(ErrorMessage.AVATAR_NOT_FOUND.getMsg());
			}
			
			Image avatar = imageManager.retrieveImageById(avatarId);
			
			respond.put("userId", nextUserId);
			respond.put("image", avatar.getImageData());
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}


}
