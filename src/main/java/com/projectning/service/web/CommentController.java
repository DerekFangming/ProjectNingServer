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

import com.projectning.service.domain.Comment;
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
			int mentionedUserId = Util.nullInt;
			try{
				type = (String)request.get("type");
				mappingId = (int)request.get("mappingId");
			}catch(NullPointerException e){
				//
			}
			
			try{
				mentionedUserId = (int)request.get("mentionedUserId");
			}catch(NullPointerException e){
				//
			}
			
			commentManager.saveComment(body, type, mappingId, userId, mentionedUserId);
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_comment_by_id")
    public ResponseEntity<Map<String, Object>> getCommentById(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			
			Comment comment = commentManager.getCommentById((int)request.get("commentId"));
			
			respond.put("commentBody", comment.getBody());
			respond.put("commentType", comment.getType());
			respond.put("mappingId", comment.getTypeMappingId());
			respond.put("createdAt", comment.getCreatedAt().toString());
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_comment_ids_by_type")
	public ResponseEntity<Map<String, Object>> getCommentIds(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			
			int userId = (int)request.get("userId");
			
			respond.put("idList", commentManager.getCommentIdListByType((String)request.get("type"), userId));
			
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_recent_comment_from_friends")
	public ResponseEntity<Map<String, Object>> getRecentCommentFromFriends(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int userId = userManager.validateAccessToken(request);
			
			int mappingId = (int)request.get("mappingId");
			String type = (String)request.get("type");
			
			List<Comment> commentList = commentManager.getRecentCommentFromFriends(type, mappingId, userId);
			List<Map<String, Object>> processedCommentList = new ArrayList<Map<String, Object>>();
			for(Comment c : commentList){
				Map<String, Object> processedComment = new HashMap<String, Object>();
				processedComment.put("commentId", c.getId());
				processedComment.put("commentbody", c.getBody());
				processedComment.put("mentionedUserId", c.getMentionedUserId());
				if (c.getMentionedUserId() != 0) processedComment.put("mentionedUserName", userManager.getUserDisplayedName(c.getMentionedUserId()));
				processedComment.put("ownerId", c.getOwnerId());
				processedComment.put("ownerName", userManager.getUserDisplayedName(c.getOwnerId()));
				processedComment.put("createdAt", c.getCreatedAt().toString());
				processedCommentList.add(processedComment);
			}
			
			respond.put("commentList", processedCommentList);
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/create_or_delete_comment")
	public ResponseEntity<Map<String, Object>> createOrDeleteComment(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int userId = userManager.validateAccessToken(request);
			
			String action = (String)request.get("action");
			if(action.equals("create")){
				String type = (String)request.get("type");
				int mappingId = (int)request.get("mappingId");
				String body = (String)request.get("commentBody");
				int mentionedUserId = Util.nullInt;
				try{
					mentionedUserId = (int)request.get("mentionedUserId");
				}catch(NullPointerException e){
				}
				
				commentManager.saveOrEnableComment(body, type, mappingId, userId, mentionedUserId);
				
				respond.put("error", "");
			}else if (action.equals("delete")){
				commentManager.softDeleteComment((int)request.get("commentId"), userId);
				
				respond.put("error", "");
			}else{
				respond.put("error", "No such option ");
			}
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
