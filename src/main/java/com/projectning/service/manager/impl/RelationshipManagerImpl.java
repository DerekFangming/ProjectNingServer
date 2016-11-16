package com.projectning.service.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.projectning.service.domain.UserDetail;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.RelationshipManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.RelationshipType;

@Component
public class RelationshipManagerImpl implements RelationshipManager{

	@Autowired private RelationshipDao relationshipDao;
	@Autowired private UserDao userDao;
	@Autowired private UserManager userManager;

	@Override
	public String sendFriendRequest(int senderId, int receiverId) throws IllegalStateException {
		Relationship relationship = new Relationship();
		String result = "";
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

				result = RelationshipType.FRIEND_CONFIRMED.getName();
			}
		}else{
			relationship.setConfirmed(false);
			result = RelationshipType.FRIEND_REQUESTED.getName();
		}
		
		relationship.setSenderId(senderId);
		relationship.setReceiverId(receiverId);
		relationship.setType(RelationshipType.FRIEND.getName());
		relationship.setCreatedAt(Instant.now());
		
		relationshipDao.persist(relationship);
		return result;
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
				throw new NotFoundException(ErrorMessage.NOT_FRIEND.getMsg());
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
	public int findNextUser(int userId) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.USERS, QueryType.FIND);
	    
	    QueryBuilder inner = qb.getInnerQueryBuilder(CoreTableType.RELATIONSHIPS, QueryType.FIND);
	    inner.addFirstQueryExpression(new QueryTerm(RelationshipDao.Field.SENDER_ID.name, userId));
	    inner.setReturnField(RelationshipDao.Field.RECEIVER_ID.name);
	    
	    qb.addFirstQueryExpression(new InnerQueryTerm(UserDao.Field.ID.name, RelationalOpType.NIN, inner));
	    qb.addNextQueryExpression(LogicalOpType.AND ,new QueryTerm(UserDao.Field.ID.name, RelationalOpType.NEQ, userId));
	    qb.setLimit(1);
	    try{
	    	return userDao.findAllObjects(qb.createQuery()).get(0).getId();
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_USER.getMsg());
	    } 
	}

	@Override
	public List<Integer> getFriendIDList(int userId) {
		List<Integer> idList = new ArrayList<Integer>();
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RelationshipDao.Field.SENDER_ID.getQueryTerm(userId));
		terms.add(RelationshipDao.Field.TYPE.getQueryTerm(RelationshipType.FRIEND.getName()));
		terms.add(RelationshipDao.Field.CONFIRMED.getQueryTerm(true));
		try{
			List<Relationship> results = relationshipDao.findAllObjects(terms);
			for(Relationship r : results){
				idList.add(r.getReceiverId());
			}
			return idList;
		}catch(NotFoundException e){
			return idList;
		}
	}
	
	@Override
	public List<Map<String, Object>> getSortedFriendList(int userId){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		List<Integer> idList = getFriendIDList(userId);
		if(idList.size() == 0){
			return result;
		}
		
		for(int i : idList){
			Map<String, Object> listItem = new HashMap<String, Object>();
			String name;
			try{
				
				UserDetail userDetail = userManager.getUserDetail(userId);
				if(!userDetail.getNickname().equals("")){
					name = userDetail.getNickname();
				}else if(!userDetail.getName().equals("")){
					name = userDetail.getName();
				}else{
					throw new NotFoundException();
				}
			}catch(NotFoundException e){
				try{
					name = userManager.getUsername(i);
				}catch(NotFoundException ex){
					throw new IllegalStateException(ErrorMessage.INTERNAL_LOGIC_ERROR.getMsg());
				}
			}
			listItem.put("id", i);
			listItem.put("name", name.substring(0, 1).toUpperCase() + name.substring(1));
			
			result.add(listItem);
		}
		
		return result;
	}

}
