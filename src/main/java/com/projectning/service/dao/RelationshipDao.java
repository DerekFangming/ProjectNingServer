package com.projectning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.domain.Relationship;
import com.projectning.util.Pair;

public interface RelationshipDao extends CommonDao<Relationship>{
	enum Field implements DaoFieldEnum{
		ID(true),
	    SENDER_ID,
	    RECEIVER_ID,
	    CONFIRMED,
		TYPE,
	    CREATED_AT,;
		
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
		    new Pair<Enum<?>, String>(Field.SENDER_ID, "INTEGER NOT NULL"),
		    new Pair<Enum<?>, String>(Field.RECEIVER_ID, "INTEGER NOT NULL"),
		    new Pair<Enum<?>, String>(Field.CONFIRMED, "BOOLEAN NOT NULL"),
			new Pair<Enum<?>, String>(Field.TYPE, "TEXT"),
		    new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"));

}
