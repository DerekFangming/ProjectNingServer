package com.projectning.service.dao.impl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.projectning.service.dao.CommonDao;
import com.projectning.service.dao.SchemaTable;
import com.projectning.service.dao.SdkDataSource;
import com.projectning.service.dao.impl.LogicalOpType;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryInstance;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.RelationalOpType;
import com.projectning.service.dao.impl.SdkDataSourceImpl;
import com.projectning.service.domain.EnumType;
import com.projectning.service.domain.HasBuilder;
import com.projectning.service.domain.HasExternalReference;
import com.projectning.service.domain.ObjectOrigin;
import com.projectning.service.exceptions.AlreadyExistsException;
import com.projectning.service.exceptions.ExceptionsHelper;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.exceptions.NotUsingObjectBuilderException;

//public abstract class JdbcBaseDao<T extends Object> implements CommonDao<T>
/**
 *
 * @param <T> Domain object Type
 */
public abstract class JdbcBaseDao<T extends Object> implements CommonDao<T>
{
  protected SchemaTable myTable;
  protected String myPkName;
  protected String TABLE_NAME;
  protected List<String> COLUMN_NAMES;
  protected String[] RETURN_PK_NAME;
  
  protected NamedParameterJdbcTemplate namedTemplate;
  protected JdbcTemplate jdbcTemplate;

  private boolean calledOnce = false;
  
  private int expectedValues;

  private SdkDataSourceImpl dataSource;
  
  // NOTE: Cannot wire in a subclass here. Thus could not do an automatic
  // creation of an ExternalReference should T be one of the ExternalReferenceTypes. 
//  @Autowired private ExternalReferenceDao externalReferenceDao;
  protected JdbcBaseDao(SdkDataSource ds, SchemaTable table)
  {
//    this(table.setDataSource(ds));
    this(table);
  }
  
  protected JdbcBaseDao(SchemaTable table)
  {
    this.dataSource = (SdkDataSourceImpl) table.getDataSource();
    
    this.myTable  = table;
    
    this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    
    TABLE_NAME              = this.myTable.getTableName();
    COLUMN_NAMES            = this.myTable.getColumnNames();

//    UPDATE_SQL_PREFIX       = "UPDATE " + TABLE_NAME + " ";
    
    if(this.myTable.hasPrimaryKey())
    {
      this.myPkName       = this.myTable.getPrimaryKeyName();
      RETURN_PK_NAME      = new String[]{this.myPkName};
    }
    else
    {
      this.myPkName       = null;
      RETURN_PK_NAME      = null;
    }
    
    initOnce(this.dataSource, TABLE_NAME, COLUMN_NAMES);
    
    // Number of columns expected for using INSERT_SQL
    this.expectedValues = this.myTable.getColumnCount();
    
    if(this.myTable.hasPrimaryKey())
    {
      --this.expectedValues;
    }

  }
    
  // TODO: Currently implemented to add to the Map ONLY when the member of
  // T is not NULL - we may need to reconsider this restriction for some
  // objects / tables.  Perhaps, we need to add NOT NULL to the table schema
  // and thus whatever is sent here is accepted.
  // SHOULD NOT include PK in returned params
  //
  // Do not add T's ID to the Map
  protected abstract NVPairList getNVPairs(T obj);
  
  // dm 10/29
//  private static MapSqlParameterSource getParamsMap(List<QueryTerm> values)
//  {
//    MapSqlParameterSource params = new MapSqlParameterSource();
//    
//    for(QueryTerm changeItem : values)
//    {
//      params.addValue(changeItem.getField(), changeItem.getValue());
//    }
//
//    return params;
//  }

  protected abstract RowMapper<T> getRowMapper( );

  @Override
  public long persist(T obj)
  {
    this.requireBuilderUsed(obj);
    
    // In the case of those domain objects that contain an xref...
    if(obj instanceof HasExternalReference)
    {
      // If it looks like the object was already persisted...
      if(((HasExternalReference) obj).isPersisted())
      {
        // Abort! ;-)
        throw new AlreadyExistsException("It looks like this ojbect has aleady been persisted: " 
        + ((HasExternalReference) obj).getXref());
      }
    }
    
    int expectedCount = this.expectedValues;
    
    if (obj instanceof EnumType)
    {
      ++expectedCount;  
    }
    
    NVPairList values = this.getNVPairs(obj);

    if(values.size() != expectedCount)
    {
      throw new IllegalArgumentException("Inserting a row into "
        + TABLE_NAME
        + " requires exactly "
        + expectedCount
        + " field values (excludes a primary key). The call to getNVPairs("
        + obj.getClass().getName()  
        + ") implementation returned "
        + values.size()
        + " field values."
        );
    }

    KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.INSERT);

    // TODO: READ BEFORE DELETING
//    // TODO: Get rid of this step by changing getParamsMap(obj) to return a List<NVPair>
//    List<NVPair> values = paramsMapToNameValueList(params);
    
//    qb.setParamsSource(values);
    qb.addNameValuePairs(values.getList());
    
    QueryInstance qi = qb.createQuery();
try {
  

    // If something goes wrong, the caller will have to deal with it.
    namedTemplate.update(qi.getQueryStr(), qi.getParams(), generatedKeyHolder, RETURN_PK_NAME);
}
catch(Throwable t)
{
  System.out.println(t.toString());
}
    long dbid = generatedKeyHolder.getKey().longValue();
      
    // Return the row's new ID
    return dbid;
  }
  
  private List<NVPair> paramsMapToNameValueList(MapSqlParameterSource map)
  {
    List<NVPair> terms = new ArrayList<NVPair>();
    
    for(String key : map.getValues().keySet())
    {
      terms.add(new NVPair(key, map.getValue(key)));
    }
    
    return terms;
  }
  // TODO: FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME
  // TODO: FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME
  // TODO: FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME
  // TODO: FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME FIX ME
  @Override
  public void update(long dbId, T obj) throws NotFoundException
  {
    NVPairList values = getNVPairs(obj);

    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.UPDATE_BY_ID);
    
    qb.addFirstQueryExpression(new QueryTerm(this.myPkName, dbId));
    qb.addNameValuePairs(values.getList());
    
    QueryInstance qi = qb.createQuery();
    
    namedTemplate.update(qi.getQueryStr(), qi.getParams());
    
//    Map<String,Object> map = params.getValues();
//        
//    String query = this.getUpdateByIdSQL(map);
//    
//    // Query string was built without the pk ID so we are not attempting
//    // to set the id value.  Now, we need it to satisfy the WHERE clause.
//    params.addValue(myPkName, dbId);
//    
//    namedTemplate.update(query, params);
  }
  
  @Override
  public void update(long dbId, NVPair value) throws NotFoundException
  {
    List<NVPair> values = convertToList(value);
    
    this.update(dbId, values);
  }
  
  @Override
  public void update(long dbId, List<NVPair> values) throws NotFoundException
  {
    QueryBuilder qb =  QueryType.getQueryBuilder(this.myTable, QueryType.UPDATE_BY_ID);
    
    qb.addFirstQueryExpression(new QueryTerm(this.myPkName, dbId));
    
    qb.addNameValuePairs(values);
      
    QueryInstance qi = qb.createQuery();
    
    namedTemplate.update(qi.getQueryStr(), qi.getParams());
    
//    MapSqlParameterSource params = getParamsMap(values);
//    String query = this.getUpdateByIdSQL(params.getValues());
//    
//    // Need to add it AFTER generating the query so we don't include this in the SET values.
//    params.addValue(this.myTable.getPrimaryKeyName(), dbId);
//    
//    namedTemplate.update(query, params);
  }
  
  @Override
  public T findById(long dbId) throws NotFoundException
  {
//    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND_BY_ID, ActiveType.ACTIVE);
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    qb.addFirstQueryExpression(new QueryTerm(myTable.getPrimaryKeyName(), RelationalOpType.EQ, dbId));
    
    QueryInstance qi = qb.createQuery();

    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));

    if(results.size() == 0)
    {
      throw new NotFoundException(
        ExceptionsHelper.dbIdNotFound(TABLE_NAME, dbId));
    }

    return results.get(0);
  }
  
//  public final static List<QueryTerm> convertPairVarArgsToList(List<QueryTerm> values)
//  {
//    List<QueryTerm> list = new ArrayList<QueryTerm>();
//    
//    for(QueryTerm value : values)
//    {
//      list.add(new QueryTerm(value));
//    }
//    
//    return list;
//  }

  @SafeVarargs
  public final static List<NVPair> convertToList(NVPair... values)
  {
    return new ArrayList<NVPair>(Arrays.asList(values));
  }
  
  @SafeVarargs
  public final static List<QueryTerm> convertToList(QueryTerm... values)
  {
    return new ArrayList<QueryTerm>(Arrays.asList(values));

//    List<QueryTerm> list = new ArrayList<QueryTerm>();
//    
//    for(QueryTerm value : values)
//    {
//      list.add(value);
//    }
//    
//    return list;
  }

  @Override
  public long findId(QueryTerm term) throws NotFoundException
  {
    return this.findId(convertToList(term));
  }
  
  @Override
  public long findId(List<QueryTerm> terms) throws NotFoundException
  {
    long dbId = -1;
    
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.FIND_ID);
    
    qb.addFirstQueryExpression(terms, LogicalOpType.AND);
    
    qb.setLimit(1);
    
    QueryInstance qi = qb.createQuery();
    
    try
    {
      SqlRowSet rows = this.namedTemplate.queryForRowSet(qi.getQueryStr(), qi.getParams());
      
      if(rows.first())
      {
        dbId = rows.getLong(myPkName);
      }
      else
      {
        throw new NotFoundException();
      }
    }
    catch(DataAccessException e)
    {
      throw new NotFoundException(e);
    }
    
    return dbId;    
  }
    

  @Override
  public List<Long> findAllIds(List<QueryTerm> terms)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.FIND_ID);
    
    qb.addFirstQueryExpression(terms, LogicalOpType.AND);
    
    QueryInstance qi = qb.createQuery();

    return findAllIds(qi);
    
//    SqlRowSet rows = this.namedTemplate.queryForRowSet(qi.getQueryStr(), qi.getParams());
//
//    List<Long> ids = new ArrayList<Long>();
//
//    while (rows.next())
//    {
//      ids.add(rows.getLong(myPkName));
//    }
//
//    return ids;    
  }
  
  @Override
  public List<Long> findAllIds(QueryInstance qi)
  {
    SqlRowSet rows = this.namedTemplate.queryForRowSet(qi.getQueryStr(), qi.getParams());

    List<Long> ids = new ArrayList<Long>();

    while (rows.next())
    {
      ids.add(rows.getLong(myPkName));
    }

    return ids;    
  }
  
  @Override
  final public List<T> findAllObjects() throws NotFoundException
  {
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    QueryInstance qi = qb.createQuery();
    
    // TODO: Previously the map param was 
    //  (MapSqlParameterSource) null
    // Confirm this way is ok:
    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));
    
    if(results.size() == 0)
    {
      throw new NotFoundException();
    }
    
    return results;
  }
  
  @Override
  public T findObject(QueryTerm value) throws NotFoundException
  {
    return this.findObject(JdbcBaseDao.convertToList(value));
  }
  
  @Override
  public T findObject(List<QueryTerm> terms) throws NotFoundException
  {
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    qb.addFirstQueryExpression(terms, LogicalOpType.AND);
    
    qb.setLimit(1);
    
    QueryInstance qi = qb.createQuery();

    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));

    if(results.size() == 0)
    {
      throw new NotFoundException();
    }

    return results.get(0);        
  }
  
  @Override
  public List<T> findAllObjects(QueryTerm term) throws NotFoundException
  {
    return this.findMultipleObjects(JdbcBaseDao.convertToList(term), 0);
  }
  
  @Override
  public List<T> findAllObjects(List<QueryTerm> terms) throws NotFoundException
  {
    return this.findMultipleObjects(terms, 0);
    
//    MapSqlParameterSource params = getParamsMap(values);
//
//    String query = this.getFindByAndedSQL(params.getValues(), 0);
//
//    List<T> results = this.namedTemplate.query(query, params, new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));
//
//    if(results.size() == 0)
//    {
//      throw new NotFoundException();
//    }
//
//    return results;    
  }

  @Override
  public List<T> findMultipleObjects(List<QueryTerm> terms, long limit) throws NotFoundException
  {
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    qb.addFirstQueryExpression(terms, LogicalOpType.AND);
    
    qb.setLimit(limit);
    
    QueryInstance qi = qb.createQuery();

    return findAllObjects(qi);
    
//    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
//      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));
//
//    if(results.size() == 0)
//    {
//      throw new NotFoundException();
//    }
//
//    return results;    
  }

  @Override
  public List<T> findMultipleObjects(List<QueryTerm> terms, long start, long limit)
  {
    throw new NotImplementedException(
      "Need to implement findMultipleObjects(terms, start, limit)");
  }
  
  @Override
  public List<T> findAllObjects(QueryInstance qi) throws NotFoundException
  {
    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));

    if(results.size() == 0)
    {
      throw new NotFoundException();
    }

    return results;    
  }

//  @Override
//  public boolean tableExists()
//  {
//    
//  }
  
  @Override
  public boolean exists(QueryTerm term)
  {    
    return this.exists(JdbcBaseDao.convertToList(term));
  }
  
  @Override
  public boolean exists(List<QueryTerm> terms)
  {
    boolean result = false;
    
    long count = this.getCount(terms);
    
    if(count != 0)
    {
      result = true;
    }
    
    return result;    
  }
    
  @Override
  public long getCount(QueryTerm term)
  {
    return this.getCount(JdbcBaseDao.convertToList(term));
  }
  
  
  @Override
  public long getCount(List<QueryTerm> terms)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, 
      QueryType.COUNT_ALL);
    
    qb.addFirstQueryExpression(terms, LogicalOpType.AND);
    
    QueryInstance qi = qb.createQuery();

    Long count = this.namedTemplate.queryForObject(qi.getQueryStr(), qi.getParams(), Long.class);
    
    return ((count == null) ? 0 : count.longValue());    
  }
  
  @Override
  public long getCount(QueryInstance qi)
  {
    Long count = this.namedTemplate.queryForObject(qi.getQueryStr(), qi.getParams(), Long.class);
    
    return ((count == null) ? 0 : count.longValue());
  }
  
  @Override
  public void requireBuilderUsed(T obj) 
  {
    if (obj instanceof HasBuilder)
    {
      HasBuilder hb = (HasBuilder) obj;

      if (hb.getObjectOrigin() != ObjectOrigin.BUILDER)
      {
        throw new NotUsingObjectBuilderException("You must use "
          + obj.getClass().getName()
          + ".Builder to create an object to persist."
          );
      }
    }
  }

  protected void initOnce(SdkDataSourceImpl dataSource, String tableName, List<String> expectedColNames)
  {
    // This is to protect us from ourselves.
    if(calledOnce)
    {
      throw new IllegalStateException("This approach doesn't work");
    }

    Set<String> dbFieldNames = new HashSet<String>();

    boolean pass = true;
    String causeMsg = null;
    
    // TODO: IF THIS WORKS, MOVE IT TO JdbcHelper
    calledOnce = true;
    
    try
    {
      //fangming: In the future, if there is an error saying FATAL too many clients here
      //It is because that too many connections are open at the same time (probably 5-10) and
      // they are not closed fast enough. We may need to change the config file for this.
      Connection connection = dataSource.getConnection();


      DatabaseMetaData metadata = connection.getMetaData();

      
      ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
      
      while (resultSet.next()) 
      {
        dbFieldNames.add(resultSet.getString("COLUMN_NAME"));
      }

      connection.close();

    }
    catch (SQLException e)
    {
      throw new IllegalStateException(e);
    }

    // If we have 0 columns in the db, the table is assumed to not exist.
    if (dbFieldNames.size() > 0)
    {
      // If the table exists, we must have at least the same number of names...
      // NOTE: The metadata appears to include name of deleted columns. Thus we can no longer
      // look for an exact size match. 
      // 
      // I _think_ the mechanism to remove those now defunct columns is to export the table, 
      // drop the table, import the table. Confirm this sometime.
      if (dbFieldNames.size() >= expectedColNames.size())
      {
        // Check they match up exactly
        for (String expectedName : expectedColNames)
        {
          if (!dbFieldNames.contains(expectedName))
          {
            pass = false;
            causeMsg = "Found an expected field name: " + expectedName + " that does not exist in the data from database";

            break;
          }
        }
      }
      else
      {
        causeMsg = "Field count mismatch";
        pass = false;
      }

      if (!pass)
      {
    	 
        throw new IllegalStateException("In table " + tableName
          + ": "
          + causeMsg);
      }
    }
  }
  
  // TODO: @NeedsAutority("SYS_ADMIN")
  @Override
  public int deleteById(long dbid)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.DELETE);
    
    qb.addFirstQueryExpression(new QueryTerm(this.myPkName, dbid));
    
    return this.delete(qb.createQuery());
  }
  
  // TODO: @NeedsAutority("SYS_ADMIN")  
  @Override
  public int delete(QueryTerm term)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.DELETE);
    
    qb.addFirstQueryExpression(term);
    
    return this.delete(qb.createQuery());
  }

  // TODO: @NeedsAutority("SYS_ADMIN")
  @Override
  public int delete(List<QueryTerm> terms)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.DELETE);
    
    qb.addFirstQueryExpression(terms, LogicalOpType.AND);
    
    return this.delete(qb.createQuery());
  }
  
  // TODO: @NeedsAutority("SYS_ADMIN")  
  @Override
  public int delete(QueryInstance qi) 
  {
    int count = this.namedTemplate.update(qi.getQueryStr(), qi.getParams()); 

    return count;    
  }
  

}
