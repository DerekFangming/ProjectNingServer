package com.projectning.service.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.MomentDao;
import com.projectning.service.dao.RelationshipDao;
import com.projectning.service.dao.UserDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.InnerQueryTerm;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryInstance;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Moment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.MomentManager;
import com.projectning.util.ErrorMessage;
import com.projectning.util.RelationshipType;

@Component
public class MomentManagerImpl implements MomentManager{

	@Autowired private MomentDao momentDao;

	@Override
	public int saveMoment(String body, int ownerId){
		
		return 0;
	}
	
	@Override
	public Moment getMoment (int senderId, int receiverId) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.USERS, QueryType.FIND);
	    
	    qb.addFirstQueryExpression(new QueryTerm(UserDao.Field.ID.name, RelationalOpType.NEQ, 12));
	    qb.addNextQueryExpression(LogicalOpType.AND ,new QueryTerm(UserDao.Field.ID.name, RelationalOpType.NEQ, 32));
	    qb.setOrdering(UserDao.Field.CREATED_AT.name, ResultsOrderType.ASCENDING);
	    qb.setLimit(1);
	    
	    QueryInstance i  = qb.createQuery();
		return null;
	}


}
