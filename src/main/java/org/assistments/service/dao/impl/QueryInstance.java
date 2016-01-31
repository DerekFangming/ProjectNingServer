package org.assistments.service.dao.impl;

import org.assistments.util.Pair;
import org.assistments.util.Util;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class QueryInstance extends Pair<String, MapSqlParameterSource>
{

  QueryInstance(String f, MapSqlParameterSource s)
  {
    super(f, s);
  }

  public String getQueryStr()
  {
    return this.getFirst();
  }
  
  public MapSqlParameterSource getParams()
  {
    return this.getSecond();
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(this.getFirst())
      .append(Util.NL)
      .append(this.getSecond().getValues().toString());
    
    return sb.toString();
  }
}
