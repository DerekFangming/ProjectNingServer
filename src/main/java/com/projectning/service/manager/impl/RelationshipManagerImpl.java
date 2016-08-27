package com.projectning.service.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.RelationshipDao;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.domain.Relationship;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.RelationshipType;

@Component
public class RelationshipManagerImpl implements RelationshipManager{

	@Autowired private RelationshipDao relationshipDao;

	@Override
	public void sendFriendRequest(int senderId, int receiverId) throws IllegalStateException {
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(senderId));
		terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(receiverId));
		terms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
		
		if(relationshipDao.exists(terms)){
			List<QueryTerm> checkBackTerms = new ArrayList<QueryTerm>();
			checkBackTerms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
			checkBackTerms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
			checkBackTerms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
			if(relationshipDao.exists(checkBackTerms)){
				throw new IllegalStateException(ErrorMessage.ALREADY_FRIEND.getMsg());
			}
		}
		
		Relationship relationship = new Relationship();
		relationship.setSenderId(senderId);
		relationship.setReceiverId(receiverId);
		relationship.setType(RelationshipType.FRIEND.getName());
		relationship.setCreatedAt(Instant.now());
		
		relationshipDao.persist(relationship);
		
	}
	
	@Deprecated
	@Override
	public void acceptFriendRequest(int senderId, int receiverId) throws IllegalStateException {
		sendFriendRequest(receiverId, senderId);
	}
	
	@Override
	public void removeFriend(int senderId, int receiverId) throws NotFoundException {
		boolean exceptionFlag = false;
		try{
			List<QueryTerm> terms = new ArrayList<QueryTerm>();
			terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(senderId));
			terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(receiverId));
			terms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
			Relationship relationship = relationshipDao.findObject(terms);
			
			relationshipDao.deleteById(relationship.getId());
		}catch (NotFoundException e){
			exceptionFlag = true;
		}
		
		try{
			List<QueryTerm> terms = new ArrayList<QueryTerm>();
			terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
			terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
			terms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
			Relationship relationship = relationshipDao.findObject(terms);
			
			relationshipDao.deleteById(relationship.getId());
		}catch (NotFoundException e){
			if(exceptionFlag){
				throw e;
			}
		}
	}

}
