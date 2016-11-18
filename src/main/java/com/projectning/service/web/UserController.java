package com.projectning.service.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.domain.UserDetail;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.exceptions.SessionExpiredException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.Util;

@Controller
public class UserController {
	
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/get_user_detail")
    public ResponseEntity<Map<String, Object>> getUserDetail(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String accessToken = (String) request.get("accessToken");
			Map<String, Object> result = helperManager.decodeJWT(accessToken);
			
			helperManager.checkSessionTimeOut((String)result.get("expire"));
			
			UserDetail detail = userManager.getUserDetail((int)request.get("userId"));
			
			respond.put("name", Util.nullToEmptyString(detail.getName()));
			respond.put("nickname", Util.nullToEmptyString(detail.getNickname()));
			respond.put("age", detail.getAge());
			respond.put("gender", Util.nullToEmptyString(detail.getGender()));
			respond.put("location", Util.nullToEmptyString(detail.getLocation()));
			respond.put("whatsUp", Util.nullToEmptyString(detail.getWhatsUp()));
			
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
