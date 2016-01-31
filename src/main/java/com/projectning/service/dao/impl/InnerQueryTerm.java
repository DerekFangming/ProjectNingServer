package com.projectning.service.dao.impl;

public class InnerQueryTerm extends QueryTerm
{
  public InnerQueryTerm(String field, QueryBuilder qb)
  {
    super(field, RelationalOpType.IN, qb);
  }

  @Override
  public QueryBuilder getValue()
  {
    return (QueryBuilder) super.getValue();
  }
}
