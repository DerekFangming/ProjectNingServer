package com.projectning.service.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.manager.RelationshipManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.Util;

@Controller
public class RelationshipController {
	
	@Autowired private RelationshipManager relationshipManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/send_friend_request")
    public ResponseEntity<Map<String, Object>> sendFriendRequest(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int id = userManager.validateAccessToken(request);
			
			userManager.checkUserIdExistance((int)request.get("receiverId"));
			
			relationshipManager.sendFriendRequest(id, (int)request.get("receiverId"));
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/get_friend_id_list")
	public ResponseEntity<Map<String, Object>> getFriendList(@RequestBody Map<String, Object> request){
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int id = userManager.validateAccessToken(request);
			
			List<Integer> idList = relationshipManager.getFriendIDList(id);
			
			respond.put("friendList", idList);
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_sorted_friend_list")
	public ResponseEntity<Map<String, Object>> getSortedFriendList(@RequestBody Map<String, Object> request){
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int id = userManager.validateAccessToken(request);
			
			List<Map<String, Object>> friendList = relationshipManager.getSortedFriendList(id);
			
			
			respond.put("friendList", friendList);
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
