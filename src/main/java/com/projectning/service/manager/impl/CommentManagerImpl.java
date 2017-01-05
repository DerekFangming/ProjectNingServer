package com.projectning.service.manager.impl;

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
	public int saveComment(String body, String type, int typeMappingId, int ownerId) {
		Comment comment = new Comment();
		comment.setBody(body);
		comment.setType(type);
		comment.setTypeMappingId(typeMappingId);
		comment.setOwnerId(ownerId);
		comment.setEnabled(true);
		comment.setCreatedAt(Instant.now());
		return commentDao.persist(comment);
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
	public List<Integer> getRecentCommentFromFriends(String type, int mappingId, int ownerId) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.COMMENTS, QueryType.FIND);
	    qb.addFirstQueryExpression(new QueryTerm(CommentDao.Field.TYPE.name, RelationalOpType.EQ, type));
	    qb.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(CommentDao.Field.TYPE_MAPPING_ID.name, RelationalOpType.EQ, mappingId));
	    
	    QueryBuilder inner = qb.getInnerQueryBuilder(CoreTableType.RELATIONSHIPS, QueryType.FIND);
	    inner.addFirstQueryExpression(new QueryTerm(RelationshipDao.Field.SENDER_ID.name, ownerId));
	    inner.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm("receiver_id = comments.owner_id"));
	    inner.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(RelationshipDao.Field.TYPE.name, RelationalOpType.EQ, RelationshipType.FRIEND.getName()));
	    inner.addNextQueryExpression(LogicalOpType.AND, 
	    		new QueryTerm(RelationshipDao.Field.CONFIRMED.name, RelationalOpType.EQ, true));
	    inner.setReturnField("1");
	    
	    qb.addNextQueryExpression(LogicalOpType.AND, new ExistQueryTerm(inner));
		try{
			return commentDao.findAllIds(qb.createQuery());
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMsg());
		}
	}

}
