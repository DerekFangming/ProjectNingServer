package com.projectning.service.dao;

import java.util.Arrays;
import java.util.List;

import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.domain.WcReport;
import com.projectning.util.Pair;

public interface WcReportDao extends CommonDao<WcReport>{
	enum Field implements DaoFieldEnum{
		ID(true),
		USER_ID,
		MENU_ID,
	    EMAIL,
	    REPORT,
	    CREATED_AT;
		
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
		    new Pair<Enum<?>, String>(Field.USER_ID, "INTEGER"),
		    new Pair<Enum<?>, String>(Field.MENU_ID, "INTEGER NOT NULL"),
		    new Pair<Enum<?>, String>(Field.EMAIL, "TEXT"),
		    new Pair<Enum<?>, String>(Field.REPORT, "TEXT"),
		    new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE"));

}
