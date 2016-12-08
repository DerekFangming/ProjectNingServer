package com.projectning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.domain.Image;
import com.projectning.util.Pair;

public interface ImageDao extends CommonDao<Image>{
	
	enum Field implements DaoFieldEnum{
		ID(true),
	    LOCATION,
	    TYPE,
	    TYPE_MAPPING_ID,
	    OWNER_ID,
	    CREATED_AT,
	    ENABLED,
	    TITLE;
		
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
		    new Pair<Enum<?>, String>(Field.LOCATION, "TEXT NOT NULL"),
		    new Pair<Enum<?>, String>(Field.TYPE, "TEXT"),
		    new Pair<Enum<?>, String>(Field.TYPE_MAPPING_ID, "INTEGER"),
		    new Pair<Enum<?>, String>(Field.OWNER_ID, "INTEGER NOT NULL"),
		    new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"),
		    new Pair<Enum<?>, String>(Field.ENABLED, "BOOLEAN NOT NULL"),
		    new Pair<Enum<?>, String>(Field.TITLE, "TEXT"));

}
