package com.projectning.service.web;

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

@Controller
public class ImageController {
	
	@Autowired private HelperManager helperManager;
	
	@RequestMapping("/upload_image")
    public ResponseEntity<Map<String, Object>> registerForSalt(@RequestBody String request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			String accessToken = jsonObj.getString("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrest");
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", "Access token is broken");
		}catch(NotFoundException e){
			respond.put("error", "User not found");
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}

}
