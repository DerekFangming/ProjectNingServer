package com.projectning.service.dao.impl;

public class InnerQueryTerm extends QueryTerm
{
  public InnerQueryTerm(String field, RelationalOpType op, QueryBuilder qb)
  {
    super(field, op, qb);
  }

  @Override
  public QueryBuilder getValue()
  {
    return (QueryBuilder) super.getValue();
  }
}
