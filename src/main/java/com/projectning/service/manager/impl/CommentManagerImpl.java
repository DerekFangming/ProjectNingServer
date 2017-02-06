package com.projectning.service.manager.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.CommentDao;
import com.projectning.service.dao.RelationshipDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.ExistQueryTerm;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Comment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.CommentManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.RelationshipType;
import com.projectning.util.Util;

@Component
public class CommentManagerImpl implements CommentManager{
	
	@Autowired private CommentDao commentDao;

	@Override
	public int saveComment(String body, String type, int typeMappingId, int ownerId, int mentionedUserId) {
		Comment comment = new Comment();
		comment.setBody(body);
		comment.setMentionedUserId(mentionedUserId);
		comment.setType(type);
		comment.setTypeMappingId(typeMappingId);
		comment.setOwnerId(ownerId);
		comment.setEnabled(true);
		comment.setCreatedAt(Instant.now());
		return commentDao.persist(comment);
	}
	
	public int saveOrEnableComment(String body, String type, int typeMappingId, int ownerId, int mentionedUserId) 
			throws IllegalStateException{
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(CommentDao.Field.BODY.getQueryTerm(body));
		values.add(CommentDao.Field.TYPE.getQueryTerm(type));
		if(typeMappingId != Util.nullInt)
			values.add(CommentDao.Field.TYPE_MAPPING_ID.getQueryTerm(typeMappingId));
		values.add(CommentDao.Field.OWNER_ID.getQueryTerm(ownerId));
		if(mentionedUserId != Util.nullInt)
			values.add(CommentDao.Field.MENTIONED_USER_ID.getQueryTerm(mentionedUserId));
		try{
			Comment comment = commentDao.findObject(values);
			if (comment.getEnabled()){
				throw new IllegalStateException(ErrorMessage.COMMENT_ALREADY_EXISTS.getMsg());
			}else{
				List<NVPair> newValues = new ArrayList<NVPair>();
				newValues.add(new NVPair(CommentDao.Field.ENABLED.name, true));
				newValues.add(new NVPair(CommentDao.Field.CREATED_AT.name, Timestamp.from(Instant.now())));
				commentDao.update(comment.getId(), newValues);
				return comment.getId();
			}
		}catch(NotFoundException e){
			return saveComment(body, type, typeMappingId, ownerId, mentionedUserId);
		}
	}

	@Override
	public Comment getCommentById(int commentId) throws NotFoundException {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(CommentDao.Field.ID.getQueryTerm(commentId));
		values.add(CommentDao.Field.ENABLED.getQueryTerm(true));
		try{
			return commentDao.findObject(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMsg());
		}
	}

	@Override
	public void softDeleteComment(int commentId, int ownerId) throws NotFoundException, IllegalStateException {
		QueryTerm value = CommentDao.Field.ID.getQueryTerm(commentId);
		Comment comment;
		try{
			comment = commentDao.findObject(value);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.NO_COMMENT_TO_DELETE.getMsg());
		}
		
		if(comment.getOwnerId() != ownerId)
			throw new IllegalStateException(ErrorMessage.UNAUTHORIZED_COMMENT_DELETE.getMsg());
		
		NVPair pair = new NVPair(CommentDao.Field.ENABLED.name, false);
		commentDao.update(comment.getId(), pair);
		
	}

	@Override
	public List<Integer> getCommentIdListByType(String type, int ownerId) throws NotFoundException {
		return getCommentIdListByTypeAndMappingId(type, Util.nullInt, ownerId);
	}

	@Override
	public List<Integer> getCommentIdListByTypeAndMappingId(String type, int mappingId, int ownerId)
			throws NotFoundException {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(CommentDao.Field.TYPE.getQueryTerm(type));
		values.add(CommentDao.Field.OWNER_ID.getQueryTerm(ownerId));
		values.add(CommentDao.Field.ENABLED.getQueryTerm(true));
		if(mappingId != Util.nullInt)
			values.add(CommentDao.Field.TYPE_MAPPING_ID.getQueryTerm(mappingId));
		
		try{
			return commentDao.findAllIds(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMsg());
		}
	}

	@Override
	public List<Comment> getRecentCommentFromFriends(String type, int mappingId, int userId) throws NotFoundException {
		
		QueryBuilder qb = getRecentCommentsHelper(type, mappingId, userId);
	    
		try{
			return commentDao.findAllObjects(qb.createQuery());
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMsg());
		}
	}

	@Override
	public int getRecentCommentCountFromFriends(String type, int mappingId, int userId){
		
		QueryBuilder qb = getRecentCommentsHelper(type, mappingId, userId);
	    
		try{
			//TODO: Why get count doesn't work
			return commentDao.findAllObjects(qb.createQuery()).size();
		}catch(NotFoundException e){
			return 0;
		}
	}
	
	private QueryBuilder getRecentCommentsHelper(String type, int mappingId, int userId){
		/* This is the example query from the following query builder
	     * SELECT * FROM comments where type = 'Feed Like' and type_mapping_id = 3 and enabled = true and (exists
		 * (select 1 from relationships where sender_id = 11 and receiver_id = comments.owner_id 
		 * and type = 'Friend' and enabled = true)
		 * or (owner_id = 11))
	     */
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.COMMENTS, QueryType.FIND);
	    qb.addFirstQueryExpression(new QueryTerm(CommentDao.Field.TYPE.name, RelationalOpType.EQ, type));
	    qb.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(CommentDao.Field.TYPE_MAPPING_ID.name, RelationalOpType.EQ, mappingId));
	    qb.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(CommentDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    
	    QueryBuilder inner = qb.getInnerQueryBuilder(CoreTableType.RELATIONSHIPS, QueryType.FIND);
	    inner.addFirstQueryExpression(new QueryTerm(RelationshipDao.Field.SENDER_ID.name, userId));
	    inner.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm("receiver_id = comments.owner_id"));
	    inner.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(RelationshipDao.Field.TYPE.name, RelationalOpType.EQ, RelationshipType.FRIEND.getName()));
	    inner.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(RelationshipDao.Field.CONFIRMED.name, RelationalOpType.EQ, true));
	    inner.setReturnField("1");
	    qb.addNextQueryExpression(LogicalOpType.AND, new ExistQueryTerm(inner));
	    /*------------------------------Or query------------------------------*/
	    qb.addNextQueryExpression(LogicalOpType.OR, 
	    		new QueryTerm(CommentDao.Field.OWNER_ID.name, RelationalOpType.EQ, userId));
	    /*------------------------------Or query------------------------------*/
	    qb.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(CommentDao.Field.TYPE.name, RelationalOpType.EQ, type));
	    qb.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(CommentDao.Field.TYPE_MAPPING_ID.name, RelationalOpType.EQ, mappingId));
	    qb.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(CommentDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.setOrdering(CommentDao.Field.CREATED_AT.name, ResultsOrderType.ASCENDING);
	    return qb;
	}

}
