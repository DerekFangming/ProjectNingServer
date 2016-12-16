package com.projectning.service.web;

import java.time.Instant;
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

import com.projectning.service.domain.Moment;
import com.projectning.service.manager.MomentManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.Util;

@Controller
public class MomentController {
	
	@Autowired private UserManager userManager;
	@Autowired private MomentManager momentManager;
	
	@RequestMapping("/get_recent_moments")
    public ResponseEntity<Map<String, Object>> getRecentMoments(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			int userId = (int)request.get("userId");
			int limit = 10;
			try{
				limit = (int)request.get("limit");
			}catch(NullPointerException e){
				//
			}
			Instant checkPoint = Instant.now();
			try{
				String timeStr = (String)request.get("checkPoint");
				checkPoint = Instant.parse(timeStr);
			}catch(NullPointerException e){
				//
			}
			List<Moment> momentList = momentManager.getRecentMomentByDate(userId, checkPoint, limit);
			List<Map<String, Object>> processedMomentList = new ArrayList<Map<String, Object>>();
			
			for(Moment m : momentList){
				Map<String, Object> processedMoment = new HashMap<String, Object>();
				processedMoment.put("momentId", m.getId());
				processedMoment.put("momentBody", m.getBody());
				processedMoment.put("createdAt", m.getCreatedAt().toString());
				processedMomentList.add(processedMoment);
			}
			respond.put("momentList", processedMomentList);
			respond.put("checkPoint", momentList.get(momentList.size() - 1).getCreatedAt().toString());
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}

}
