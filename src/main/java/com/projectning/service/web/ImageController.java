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
import com.projectning.service.manager.UserManager;
import com.projectning.util.Util;

@Controller
public class ImageController {
	
	@Autowired private ImageManager imageManager;
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
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
				
			}
			
			String verifiedType = Util.verifyType((String)request.get("type"));
			
			imageManager.saveImage((String)request.get("image"), verifiedType, id, title);
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", "User not found");
		}catch (FileNotFoundException e) {
			respond.put("error", "Internal error, image path not found");
		}catch (IOException e) {
			respond.put("error", "Internal error, cannot write image file");
		}catch(SessionExpiredException e){
			respond.put("error", "Session timeout");
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
			respond.put("image", image.getLocation());// crazy hack
			respond.put("title", image.getTitle());
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", "Image not found");
		}catch (FileNotFoundException e) {
			respond.put("error", "Internal error, image path not found");
		}catch (IOException e) {
			respond.put("error", "Internal error, cannot write image file");
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
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", "Image not found");
		}catch(SessionExpiredException e){
			respond.put("error", "Session timeout");
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
			
			respond.put("idList", imageManager.getImageIdByType((String)request.get("type"), id));
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", "Image not found");
		}catch(SessionExpiredException e){
			respond.put("error", "Session timeout");
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
