package com.projectning.service.manager.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.dao.FeedDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Feed;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.FeedManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.ImageType;

@Component
public class FeedManagerImpl implements FeedManager{

	@Autowired private FeedDao momentDao;

	@Override
	public int saveMoment(String body, int ownerId){
		
		Feed moment = new Feed();
		moment.setBody(body);
		moment.setEnabled(true);
		moment.setOwnerId(ownerId);
		moment.setCreatedAt(Instant.now());
		
		return momentDao.persist(moment);
	}
	
	@Override
	public Feed getMomentById(int momentId) throws NotFoundException {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(FeedDao.Field.ID.getQueryTerm(momentId));
		values.add(FeedDao.Field.ENABLED.getQueryTerm(true));
		
		try{
			return momentDao.findObject(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.MOMENT_NOT_FOUND.getMsg());
		}
	}
	
	@Override
	public void softDeleteMoment(int momentId, int ownerId) throws NotFoundException, IllegalStateException{
		QueryTerm value = FeedDao.Field.ID.getQueryTerm(momentId);
		Feed moment;
		try{
			moment = momentDao.findObject(value);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.NO_MOMENT_TO_DELETE.getMsg());
		}
		
		if(moment.getOwnerId() != ownerId)
			throw new IllegalStateException(ErrorMessage.UNAUTHORIZED_MOMENT_DELETE.getMsg());
		
		NVPair pair = new NVPair(FeedDao.Field.ENABLED.name, false);
		momentDao.update(moment.getId(), pair);
	}
	
	@Override
	public List<Feed> getRecentMomentByDate (int ownerId, Instant date, int limit) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.CREATED_AT.name, RelationalOpType.LT, Timestamp.from(date)));
	    qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    
	    try{
	    	return momentDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_MOMENTS_FOUND.getMsg());
	    }
	}

	@Override
	public List<Integer> getMomentPreviewImageIdList(int ownerId){
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.IMAGES, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(ImageDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(ImageDao.Field.TYPE.name, RelationalOpType.EQ, ImageType.MOMENT_COVER.getName()));
	    qb.setOrdering(ImageDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(4);
	    
	    try{
	    	return momentDao.findAllIds(qb.createQuery());
	    }catch(NotFoundException e){
	    	return new ArrayList<Integer>();
	    }
	}

}
