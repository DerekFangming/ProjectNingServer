package org.assistments.service.dao;

import java.util.Arrays;
import java.util.List;

import org.assistments.service.dao.impl.QueryTerm;
import org.assistments.service.dao.impl.RelationalOpType;
import org.assistments.service.domain.User;
import org.assistments.util.Pair;

public interface UserDao extends CommonDao<User>{
	enum Field implements DaoFieldEnum{
		ID(true),
	    USERNAME,
	    PASSWORD;
		
		public boolean isPK = false;
	    public String name;
	  
	    Field(boolean isPK)
	    {
	      this.isPK = isPK;
	      this.name = this.name().toLowerCase();
	    }
	  
	    Field()
	    {
	      this(false);
	    }
	    
	    @Override
	    public QueryTerm getQueryTerm(Object value)
	    {
	      return new QueryTerm(this.name, value);
	    }

	    @Override
	    public QueryTerm getQueryTerm(RelationalOpType op, Object value)
	    {
	      return new QueryTerm(this.name, op, value);
	    }
	}
	
	List<Pair<Enum<?>, String>> FieldTypes = Arrays.asList(
		    new Pair<Enum<?>, String>(Field.ID, "SERIAL NOT NULL"),
		    new Pair<Enum<?>, String>(Field.USERNAME, "TEXT NOT NULL"),
		    new Pair<Enum<?>, String>(Field.PASSWORD, "TEXT NOT NULL"));

}
