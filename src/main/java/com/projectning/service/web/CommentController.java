package com.projectning.service.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.manager.CommentManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.Util;

@Controller
public class CommentController {
	
	@Autowired private UserManager userManager;
	@Autowired private CommentManager commentManager;
	
	@RequestMapping("/create_comment")
    public ResponseEntity<Map<String, Object>> createComment(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int userId = userManager.validateAccessToken(request);
			
			String body = (String)request.get("commentBody");
			String type = "";
			int mappingId = Util.nullInt;
			try{
				type = (String)request.get("type");
				mappingId = (int)request.get("mappingId");
			}catch(NullPointerException e){
				//
			}
			
			commentManager.saveComment(body, type, mappingId, userId);
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
