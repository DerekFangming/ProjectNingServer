package com.projectning.service.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.RelationshipDao;
import com.projectning.service.dao.UserDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.InnerQueryTerm;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.domain.Relationship;
import com.projectning.service.domain.User;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.RelationshipType;

@Component
public class RelationshipManagerImpl implements RelationshipManager{

	@Autowired private RelationshipDao relationshipDao;
	@Autowired private UserDao userDao;

	@Override
	public void sendFriendRequest(int senderId, int receiverId) throws IllegalStateException {
		Relationship relationship = new Relationship();
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(senderId));
		terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(receiverId));
		terms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
		
		if(relationshipDao.exists(terms)){
			relationship.setConfirmed(true);
			
			List<QueryTerm> checkBackTerms = new ArrayList<QueryTerm>();
			checkBackTerms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
			checkBackTerms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
			checkBackTerms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
			if(relationshipDao.exists(checkBackTerms)){
				throw new IllegalStateException(ErrorMessage.ALREADY_FRIEND.getMsg());
			}else{
				Relationship rel = relationshipDao.findObject(terms);
				NVPair newValue = new NVPair(RelationshipDao.Field.CONFIRMED.name, true);
				relationshipDao.update(rel.getId(), newValue);

			}
		}else{
			relationship.setConfirmed(false);
		}
		
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

	@Override
	public void denyUser(int senderId, int receiverId) throws IllegalStateException{
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RelationshipDao.Field.RECEIVER_ID.getQueryTerm(receiverId));
		terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(senderId));
		terms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.DENIED.getName()));
		if(relationshipDao.exists(terms)){
			throw new IllegalStateException(ErrorMessage.ALREADY_DENIED.getMsg());
		}
		
		Relationship relationship = new Relationship();
		relationship.setSenderId(senderId);
		relationship.setReceiverId(receiverId);
		relationship.setConfirmed(true);
		relationship.setType(RelationshipType.DENIED.getName());
		relationship.setCreatedAt(Instant.now());
	}

	@Override
	public void findNextUser(int userId) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.USERS, QueryType.FIND);
	    
	    QueryBuilder inner = qb.getInnerQueryBuilder(CoreTableType.RELATIONSHIPS, QueryType.FIND);
	    inner.addFirstQueryExpression(new QueryTerm(RelationshipDao.Field.SENDER_ID.name, userId));
	    
	    inner.setReturnField(RelationshipDao.Field.RECEIVER_ID.name);
	    
	    qb.addFirstQueryExpression(new InnerQueryTerm(UserDao.Field.ID.name, RelationalOpType.NIN, inner));
	    qb.addNextQueryExpression(LogicalOpType.AND ,new QueryTerm(UserDao.Field.ID.name, RelationalOpType.NEQ, userId));
	    
	    List<User> temp = userDao.findAllObjects(qb.createQuery());
	    
	    for(User u : temp){
	    	System.out.println(u.getId());
	    }
		
	}

}
