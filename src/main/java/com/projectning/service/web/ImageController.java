package com.projectning.service.web;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.projectning.service.exceptions.SessionExpiredException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.ImageManager;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.Util;

@Controller
public class ImageController {
	
	@Autowired private ImageManager imageManager;
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	@Autowired private RelationshipManager relationshipManager;
	
	@RequestMapping("/upload_image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String) request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			String title = "";
			try{
				title = (String)request.get("title");
			}catch(NullPointerException e){
				//
			}
			
			String verifiedType = Util.verifyImageType((String)request.get("type"));
			
			imageManager.saveImage((String)request.get("image"), verifiedType, id, title);
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.USER_NOT_FOUND.getMsg());
		}catch (FileNotFoundException e) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_PATH.getMsg());
		}catch (IOException e) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_IO.getMsg());
		}catch(SessionExpiredException e){
			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/download_image_by_id")
    public ResponseEntity<Map<String, Object>> downloadImageById(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String)request.get("accessToken");
			int imageId = (int)request.get("imageId");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			Image image = imageManager.retrieveImageById(imageId, id);
			
			respond.put("error", "");
			respond.put("createdAt", image.getCreatedAt().toString());
			respond.put("image", image.getImageData());
			respond.put("title", image.getTitle());
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.IMAGE_NOT_FOUND.getMsg());
		}catch (FileNotFoundException e) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_PATH.getMsg());
		}catch (IOException e) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_IO.getMsg());
		}catch(SessionExpiredException e){
			respond.put("error", "Session timeout");
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/delete_image")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String)request.get("accessToken");
			int imageId = (int)request.get("imageId");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			imageManager.softDeleteImage(imageId, id);
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.IMAGE_NOT_FOUND.getMsg());
		}catch(SessionExpiredException e){
			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/get_image_ids_by_type")
	public ResponseEntity<Map<String, Object>> getImageIds(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String)request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			respond.put("idList", imageManager.getImageIdListByType((String)request.get("type"), id));
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.IMAGE_NOT_FOUND.getMsg());
		}catch(SessionExpiredException e){
			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_next_avatar")
	public ResponseEntity<Map<String, Object>> getAvatar(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String)request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int senderId = userManager.getUserId((String)result.get("username"), accessToken);
			
			try{
				int receiverId = (int)request.get("userId");
				String action = (String)request.get("action");
				
				userManager.checkUserIdExistance(receiverId);
				
				if(action.equals("deny")){
					relationshipManager.denyUser(senderId, receiverId);
				}else if(action.equals("friend")){
					relationshipManager.sendFriendRequest(senderId, receiverId);
				}
			}catch(NullPointerException npe){
				//
			}
			
			int nextUserId = relationshipManager.findNextUser(senderId);
			
			Image avatar = imageManager.retrieveAvatar(nextUserId);
			
			respond.put("error", "");
			respond.put("userId", nextUserId);
			respond.put("image", avatar.getImageData());
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.RESOURCE_NOT_FOUND.getMsg());
		}catch(SessionExpiredException e){
			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
		}catch (FileNotFoundException e) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_PATH.getMsg());
		}catch (IOException e) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_IO.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
