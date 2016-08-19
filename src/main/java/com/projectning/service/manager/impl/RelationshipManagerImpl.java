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

@Component
public class RelationshipManagerImpl implements RelationshipManager{

	@Autowired private RelationshipDao relationshipDao;

	@Override
	public void sendFriendRequest(int senderId, int receiverId) throws IllegalStateException {
		
		try{
			List<QueryTerm> terms = new ArrayList<QueryTerm>();
			terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(senderId));
			terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(receiverId));
			Relationship relationship = relationshipDao.findObject(terms);
			
			if(relationship.getAccepted()){
				throw new IllegalStateException(ErrorMessage.ALREADY_FRIEND.getMsg());
			}
			
			NVPair newValue = new NVPair(RelationshipDao.Field.ACCEPTED.name, true);
			relationshipDao.update(relationship.getId(), newValue);
		}catch (NotFoundException e){
			try{
				List<QueryTerm> terms = new ArrayList<QueryTerm>();
				terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
				terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
				Relationship relationship = relationshipDao.findObject(terms);
			
				if(relationship.getAccepted()){
					throw new IllegalStateException(ErrorMessage.ALREADY_FRIEND.getMsg());
				}else{
					throw new IllegalStateException(ErrorMessage.ALREADY_REQUESTED.getMsg());
				}
			}catch (NotFoundException ex){
				Relationship relationship = new Relationship();
				relationship.setSenderId(senderId);
				relationship.setReceiverId(receiverId);
				relationship.setAccepted(false);
				relationship.setCreatedAt(Instant.now());
				
				relationshipDao.persist(relationship);
			}
			
		}
		
	}
	
	@Override
	public void acceptFriendRequest(int senderId, int receiverId) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
		terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
		Relationship relationship = relationshipDao.findObject(terms);
		if(relationship.getAccepted()){
			throw new IllegalStateException(ErrorMessage.ALREADY_FRIEND.getMsg());
		}else{
			NVPair newValue = new NVPair(RelationshipDao.Field.ACCEPTED.name, true);
			relationshipDao.update(relationship.getId(), newValue);
		}
	}
	
	@Override
	public void removeFriend(int senderId, int receiverId) throws NotFoundException {
		try{
			List<QueryTerm> terms = new ArrayList<QueryTerm>();
			terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(senderId));
			terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(receiverId));
			Relationship relationship = relationshipDao.findObject(terms);
			
			relationshipDao.deleteById(relationship.getId());
		}catch (NotFoundException e){
			List<QueryTerm> terms = new ArrayList<QueryTerm>();
			terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
			terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
			Relationship relationship = relationshipDao.findObject(terms);
			
			relationshipDao.deleteById(relationship.getId());
		}
	}

}
