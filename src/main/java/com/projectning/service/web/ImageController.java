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
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.ImageManager;

@Controller
public class ImageController {
	
	@Autowired private ImageManager imageManager;
	@Autowired private HelperManager helperManager;
	
	@RequestMapping("/upload_image")
    public ResponseEntity<Map<String, Object>> registerForSalt(@RequestBody String request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			String accessToken = jsonObj.getString("accessToken");
			//Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			imageManager.saveImage(jsonObj.getString("image"), "portrait", 1);
			
			
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrest");
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", "Access token is broken");
		}catch(NotFoundException e){
			respond.put("error", "User not found");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}

}
