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

	@Autowired private FeedDao feedDao;

	@Override
	public int saveFeed(String body, int ownerId){
		
		Feed feed = new Feed();
		feed.setBody(body);
		feed.setEnabled(true);
		feed.setOwnerId(ownerId);
		feed.setCreatedAt(Instant.now());
		
		return feedDao.persist(feed);
	}
	
	@Override
	public Feed getFeedById(int feedId) throws NotFoundException {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(FeedDao.Field.ID.getQueryTerm(feedId));
		values.add(FeedDao.Field.ENABLED.getQueryTerm(true));
		
		try{
			return feedDao.findObject(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.FEED_NOT_FOUND.getMsg());
		}
	}
	
	@Override
	public void softDeleteFeed(int feedId, int ownerId) throws NotFoundException, IllegalStateException{
		QueryTerm value = FeedDao.Field.ID.getQueryTerm(feedId);
		Feed feed;
		try{
			feed = feedDao.findObject(value);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.NO_FEED_TO_DELETE.getMsg());
		}
		
		if(feed.getOwnerId() != ownerId)
			throw new IllegalStateException(ErrorMessage.UNAUTHORIZED_FEED_DELETE.getMsg());
		
		NVPair pair = new NVPair(FeedDao.Field.ENABLED.name, false);
		feedDao.update(feed.getId(), pair);
	}
	
	@Override
	public List<Feed> getRecentFeedByDate (int ownerId, Instant date, int limit) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.FEEDS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(FeedDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.CREATED_AT.name, RelationalOpType.LT, Timestamp.from(date)));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.setOrdering(FeedDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    
	    try{
	    	return feedDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_FEEDS_FOUND.getMsg());
	    }
	}

	@Override
	public List<Integer> getFeedPreviewImageIdList(int ownerId){
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.IMAGES, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(ImageDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(ImageDao.Field.TYPE.name, RelationalOpType.EQ, ImageType.FEED_COVER.getName()));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(FeedDao.Field.ENABLED.name, RelationalOpType.EQ, true));
	    qb.setOrdering(ImageDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(4);
	    
	    try{
	    	return feedDao.findAllIds(qb.createQuery());
	    }catch(NotFoundException e){
	    	return new ArrayList<Integer>();
	    }
	}

}
