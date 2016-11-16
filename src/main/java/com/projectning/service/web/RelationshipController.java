package com.projectning.service.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	@RequestMapping("/get_friend_id_list")
	public ResponseEntity<Map<String, Object>> getFriendList(@RequestBody Map<String, Object> request){
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String) request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			List<Integer> idList = relationshipManager.getFriendIDList(id);
			
			respond.put("friendList", idList);
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
	
	@RequestMapping("/get_sorted_friend_list")
	public ResponseEntity<Map<String, Object>> getSortedFriendList(@RequestBody Map<String, Object> request){
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			
			String accessToken = (String) request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			int id = userManager.getUserId((String)result.get("username"), accessToken);
			
			List<Map<String, Object>> friendList = relationshipManager.getSortedFriendList(id);
			
			/*
			Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
			
			Map<String, Object> allen = new HashMap<String, Object>();
			allen.put("name", "Allen");
			allen.put("id", 1);
			
			Map<String, Object> abe = new HashMap<String, Object>();
			abe.put("name", "Abe");
			abe.put("id", 2);
			
			Map<String, Object> bob = new HashMap<String, Object>();
			bob.put("name", "Bob");
			bob.put("id", 3);
			
			Map<String, Object> tom = new HashMap<String, Object>();
			tom.put("name", "Tom");
			tom.put("id", 4);
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			list1.add(abe);
			list1.add(allen);
			list1.add(bob);
			list1.add(tom);
			list1.add(bob);
			list1.add(tom);
			list1.add(bob);
			list1.add(tom);
			list1.add(bob);
			list1.add(tom);
			list1.add(bob);
			list1.add(tom);
			list1.add(bob);
			list1.add(tom);

			result.put("T", list3);
			result.put("B", list2);
			result.put("Z", list1);
			result.put("S", list1);
			result.put("K", list1);
			result.put("N", list1);
			result.put("V", list1);
			result.put("C", list1);
			result.put("A", list1);*/
			
			respond.put("friendList", friendList);
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
