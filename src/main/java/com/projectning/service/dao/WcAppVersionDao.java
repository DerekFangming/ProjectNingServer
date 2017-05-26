package com.projectning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.domain.WcAppVersion;
import com.projectning.util.Pair;

public interface WcAppVersionDao extends CommonDao<WcAppVersion>{
	enum Field implements DaoFieldEnum{
		ID(true),
		APP_VERSION,
		SUB_VERSION,
	    STATUS,
	    TITLE,
	    MESSAGE,
	    UPDATES;
		
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
		    new Pair<Enum<?>, String>(Field.APP_VERSION, "TEXT NOT NULL"),
		    new Pair<Enum<?>, String>(Field.SUB_VERSION, "INTEGER NOT NULL"),
		    new Pair<Enum<?>, String>(Field.STATUS, "TEXT NOT NULL"),
		    new Pair<Enum<?>, String>(Field.TITLE, "TEXT"),
		    new Pair<Enum<?>, String>(Field.MESSAGE, "TEXT"),
		    new Pair<Enum<?>, String>(Field.UPDATES, "TEXT"));

}
