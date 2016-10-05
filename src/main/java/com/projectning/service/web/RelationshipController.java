package com.projectning.service.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.exceptions.SessionExpiredException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;

@Controller
public class RelationshipController {
	
	@Autowired private RelationshipManager relationshipManager;
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/send_friend_request")
    public ResponseEntity<Map<String, Object>> sendFriendRequest(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String) request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			userManager.checkUserIdExistance((int)request.get("receiverId"));
			
			relationshipManager.sendFriendRequest(id, (int)request.get("receiverId"));
			
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", e.getMessage());
		}catch(SessionExpiredException e){
			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	

}
