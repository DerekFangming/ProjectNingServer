package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.RelationshipDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.Relationship;

@Repository
@Jdbc
public class JdbcRelationshipDao extends JdbcBaseDao<Relationship> implements RelationshipDao{
	public JdbcRelationshipDao()
	  {
	    super(CoreTableType.RELATIONSHIPS);
	  }
	
	@Override
	  protected NVPairList getNVPairs(Relationship obj)
	  {
	    NVPairList params = new NVPairList();
	    
	    params.addValue(RelationshipDao.Field.SENDER_ID.name, obj.getSenderId());
	    params.addValue(RelationshipDao.Field.RECEIVER_ID.name, obj.getReceiverId());
		params.addValue(RelationshipDao.Field.ACCEPTED.name, obj.getAccepted());
	    params.addValue(RelationshipDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<Relationship> getRowMapper( )
	  {
	    RowMapper<Relationship> rm = new RowMapper<Relationship>()
	    {
	      @Override
	      public Relationship mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  Relationship obj = new Relationship();
	    	  obj.setId(rs.getInt(RelationshipDao.Field.ID.name));
	    	  obj.setSenderId(rs.getInt(RelationshipDao.Field.SENDER_ID.name));
	    	  obj.setReceiverId(rs.getInt(RelationshipDao.Field.RECEIVER_ID.name));
	    	  obj.setCreatedAt(rs.getTimestamp(RelationshipDao.Field.CREATED_AT.name).toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
