package com.projectning.service.manager.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.MomentDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryInstance;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Moment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.MomentManager;

@Component
public class MomentManagerImpl implements MomentManager{

	@Autowired private MomentDao momentDao;

	@Override
	public int saveMoment(String body, int ownerId){
		
		return 0;
	}
	
	@Override
	public List<Moment> getRecentMomentByDate (int ownerId, Instant date, int limit) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.MOMENTS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(MomentDao.Field.OWNER_ID.name, RelationalOpType.EQ, ownerId));
	    qb.addNextQueryExpression(LogicalOpType.AND,new QueryTerm(MomentDao.Field.CREATED_AT.name, RelationalOpType.LT, date));
	    qb.setOrdering(MomentDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
	    qb.setLimit(limit);
	    
	    QueryInstance i  = qb.createQuery();
		return null;
	}


}
