package com.projectning.service.dao;

import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.RelationalOpType;

public interface DaoFieldEnum
{
  QueryTerm getQueryTerm(Object value);
  
  QueryTerm getQueryTerm(RelationalOpType op, Object value);  
}
