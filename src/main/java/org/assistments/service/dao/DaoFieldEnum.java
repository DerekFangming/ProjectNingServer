package org.assistments.service.dao;

import org.assistments.service.dao.impl.QueryTerm;
import org.assistments.service.dao.impl.RelationalOpType;

public interface DaoFieldEnum
{
  QueryTerm getQueryTerm(Object value);
  
  QueryTerm getQueryTerm(RelationalOpType op, Object value);  
}
