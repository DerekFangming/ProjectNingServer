package com.projectning.service.dao.impl;

public enum RelationalOpType
{
  EQ("=") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      // Use "super" since the private method belongs to QueryRelationalOpType
      // and this enum item is actually an inner class.
      return super.defualtMakeTerm(term);
    }
  },
  NEQ("!=") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtMakeTerm(term);
    }
  },
  GT(">") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtMakeTerm(term);
    }
  },
  GE(">=") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtMakeTerm(term);
    }
  },
  LT("<") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtMakeTerm(term);
    }
  },
  LE("<=") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtMakeTerm(term);
    }
  },
  LIKE("LIKE") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtLikeMakeTerm(term);
    }
  },
  ILIKE("ILIKE") {
    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      return super.defualtLikeMakeTerm(term);
    }
  }, 
  IN("IN"){

    @Override
    public StringBuilder makeSymbolicTerm(QueryTerm term)
    {
      InnerQueryTerm iqt = (InnerQueryTerm) term;
      
      // Get the inner QB
      QueryBuilder iqb = iqt.getValue();
      
      // Get the inner query itself
      QueryInstance qi = iqb.createQuery();

      return new StringBuilder()
        .append(term.getField())
        .append(" IN (")
        .append(qi.getQueryStr())
        .append(")");        
    }
  }, 
  NIN("NOT IN"){

	    @Override
	    public StringBuilder makeSymbolicTerm(QueryTerm term)
	    {
	      InnerQueryTerm iqt = (InnerQueryTerm) term;
	      
	      // Get the inner QB
	      QueryBuilder iqb = iqt.getValue();
	      
	      // Get the inner query itself
	      QueryInstance qi = iqb.createQuery();

	      return new StringBuilder()
	        .append(term.getField())
	        .append(" NOT IN (")
	        .append(qi.getQueryStr())
	        .append(")");        
	    }
	  },
  EXT("EXISTS"){
		  @Override
		    public StringBuilder makeSymbolicTerm(QueryTerm term)
		    {
		      ExistQueryTerm iqt = (ExistQueryTerm) term;
		      // Get the inner QB
		      QueryBuilder iqb = iqt.getValue();
		      // Get the inner query itself
		      QueryInstance qi = iqb.createQuery();

		      return new StringBuilder()
		        //.append(term.getField())
		        .append(" EXISTS (")
		        .append(qi.getQueryStr())
		        .append(")");        
		    }
	  };

  public String asString;

  public abstract StringBuilder makeSymbolicTerm(QueryTerm term);
  
  RelationalOpType(String opValue)
  {
    this.asString = " " + opValue + " :";      
  }
  
  private StringBuilder defualtMakeTerm(QueryTerm term)    
  {
    return new StringBuilder()
      .append(term.getField())
      .append(this.asString)
      .append(term.getSymbolicName());
  }

  // Via: http://stackoverflow.com/questions/3246807/spring-like-clause
  private StringBuilder defualtLikeMakeTerm(QueryTerm term)    
  {
    return new StringBuilder()
      .append(term.getField())
      .append(this.asString)
      .append("%")
      .append(term.getSymbolicName())
      .append("%");
  }
}
