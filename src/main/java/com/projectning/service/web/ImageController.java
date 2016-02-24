package com.projectning.service.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.exceptions.SessionExpiredException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.ImageManager;
import com.projectning.service.manager.UserManager;

@Controller
public class ImageController {
	
	@Autowired private ImageManager imageManager;
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/upload_image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestBody String request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			String accessToken = jsonObj.getString("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			imageManager.saveImage(jsonObj.getString("image"), "portrait", id);
			
			respond.put("error", "");
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrest");
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
	
	@RequestMapping("/download_image")
    public ResponseEntity<Map<String, Object>> downloadImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			//JsonObject jsonObj = helperManager.stringToJsonHelper(request);
//			String accessToken = jsonObj.getString("accessToken");
//			int imageId = jsonObj.getInt("imageId");
//			Map<String, Object> result = helperManager.decodeJWT(accessToken);
//			
//			helperManager.checkSessionTimeOut((String)result.get("expire"));
//			
//			int id = userManager.getUserId((String)result.get("username"), accessToken);
//			
//			String image = imageManager.retrieveImage(imageId, id);
			
			System.out.println(request.get("haha"));
			
			String image = imageManager.retrieveImage((int)request.get("imageId"), (int)request.get("userId"));
			
			respond.put("error", "");
			respond.put("image", image);
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrest");
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
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestBody String request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			String accessToken = jsonObj.getString("accessToken");
			int imageId = jsonObj.getInt("imageId");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			imageManager.softDeleteImage(imageId, id);
			
			respond.put("error", "");
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrest");
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
