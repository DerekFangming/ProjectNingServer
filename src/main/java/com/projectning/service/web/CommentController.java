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
import com.projectning.service.exceptions.NotFoundException;
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
			
			int commentId = commentManager.saveOrEnableComment(body, type, mappingId, userId, mentionedUserId);
			
			respond.put("commentId", commentId);
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/delete_comment")
	public ResponseEntity<Map<String, Object>> deleteComment(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int userId = userManager.validateAccessToken(request);
			
			commentManager.softDeleteComment((int)request.get("commentId"), userId);
			
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
			
			boolean likedByCurrentUser = false;
			List<Map<String, Object>> processedCommentList = new ArrayList<Map<String, Object>>();
			for(Comment c : commentList){
				Map<String, Object> processedComment = new HashMap<String, Object>();
				processedComment.put("commentId", c.getId());
				processedComment.put("commentbody", c.getBody());
				if (c.getMentionedUserId() != 0) {
					processedComment.put("mentionedUserId", c.getMentionedUserId());
					processedComment.put("mentionedUserName", userManager.getUserDisplayedName(c.getMentionedUserId()));
				}
				processedComment.put("ownerId", c.getOwnerId());
				processedComment.put("ownerName", userManager.getUserDisplayedName(c.getOwnerId()));
				processedComment.put("createdAt", c.getCreatedAt().toString());
				processedCommentList.add(processedComment);
				if(c.getOwnerId() == userId){
					likedByCurrentUser = true;
				}
			}
			
			respond.put("commentList", processedCommentList);
			respond.put("likedByCurrentUser", likedByCurrentUser);
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_comment_counts_from_friends")
	public ResponseEntity<Map<String, Object>> getCommentCountsFromFriends(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int userId = userManager.validateAccessToken(request);
			int mappingId = (int)request.get("mappingId");

			boolean likedByCurrentUser = false;
			int likedCommentId = 0;
			List<Comment> commentLikeList = new ArrayList<Comment>();
			try{
				commentLikeList = commentManager.getRecentCommentFromFriends("Feed Like", mappingId, userId);
				
				for(Comment c : commentLikeList){
					if(c.getOwnerId() == userId){
						likedByCurrentUser = true;
						likedCommentId = c.getId();
						break;
					}
				}
			}catch (NotFoundException e){}
			
			respond.put("likedByCurrentUser", likedByCurrentUser);
			respond.put("likedCommentId", likedCommentId);
			respond.put("commentLikeCount", commentLikeList.size());
			respond.put("commentCount", commentManager.getRecentCommentCountFromFriends("Feed", mappingId, userId));
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
