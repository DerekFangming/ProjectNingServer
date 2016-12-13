package com.projectning.service.web;

import java.time.Duration;
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
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			//int id = userManager.validateAccessToken(request);
			
			List<Moment> momentList = momentManager.getRecentMomentByDate(4, Instant.now().minus(Duration.ofDays(1)), 10);
			List<Map<String, Object>> processedMomentList = new ArrayList<Map<String, Object>>();
			
			for(Moment m : momentList){
				Map<String, Object> processedMoment = new HashMap<String, Object>();
				processedMoment.put("momenrId", m.getId());
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
