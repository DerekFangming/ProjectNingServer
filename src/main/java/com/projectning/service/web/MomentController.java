package com.projectning.service.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.manager.MomentManager;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.Util;

@Controller
public class MomentController {
	
	@Autowired private UserManager userManager;
	@Autowired private MomentManager momentManager;
	
	@RequestMapping("/test_moment")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			//int id = userManager.validateAccessToken(request);
			
			momentManager.getMoment(1, 1);
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}

}
