package com.projectning.service.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.CommentDao;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.domain.Comment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.CommentManager;
import com.projectning.util.ErrorMessage;
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

}
