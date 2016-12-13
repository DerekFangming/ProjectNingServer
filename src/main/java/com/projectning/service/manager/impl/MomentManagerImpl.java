package com.projectning.service.manager.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.MomentDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Moment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.MomentManager;
import com.projectning.util.ErrorMessage;

@Component
public class MomentManagerImpl implements MomentManager{

	@Autowired private MomentDao momentDao;

	@Override
	public int saveMoment(String body, int ownerId){
		
		Moment moment = new Moment();
		moment.setBody(body);
		moment.setEnabled(true);
		moment.setOwnerId(ownerId);
		moment.setCreatedAt(Instant.now());
		
		return momentDao.persist(moment);
	}
	
	@Override
	public Moment getMomentById(int momentId) throws NotFoundException {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(MomentDao.Field.ID.getQueryTerm(momentId));
		values.add(MomentDao.Field.ENABLED.getQueryTerm(true));
		
		try{
			return momentDao.findObject(values);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.MOMENT_NOT_FOUND.getMsg());
		}
	}
	
	@Override
	public void softDeleteImage(int momentId, int ownerId) throws NotFoundException, IllegalStateException{
		QueryTerm value = MomentDao.Field.ID.getQueryTerm(momentId);
		Moment moment;
		try{
			moment = momentDao.findObject(value);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.NO_MOMENT_TO_DELETE.getMsg());
		}
		
		if(moment.getOwnerId() != ownerId)
			throw new IllegalStateException(ErrorMessage.UNAUTHORIZED_MOMENT_DELETE.getMsg());
		
		NVPair pair = new NVPair(MomentDao.Field.ENABLED.name, false);
		momentDao.update(moment.getId(), pair);
	}
	
	@Override
	public List<Moment> getRecentMomentByDate (int ownerId, Instant date, int limit) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.MOMENTS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(MomentDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,
	    		new QueryTerm(MomentDao.Field.CREATED_AT.name, RelationalOpType.LT, Timestamp.from(date)));
	    qb.setOrdering(MomentDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    
	    try{
	    	return momentDao.findAllObjects(qb.createQuery());
	    }catch(NotFoundException e){
	    	throw new NotFoundException(ErrorMessage.NO_MORE_MOMENTS_FOUND.getMsg());
	    }
	}


}
