package com.projectning.service.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.dao.MomentDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.Moment;

@Repository
@Jdbc
public class JdbcMomentDao extends JdbcBaseDao<Moment> implements MomentDao{
	public JdbcMomentDao()
	  {
	    super(CoreTableType.MOMENTS);
	  }
	
	@Override
	  protected NVPairList getNVPairs(Moment obj)
	  {
	    NVPairList params = new NVPairList();
	    
		params.addValue(MomentDao.Field.BODY.name, obj.getBody());
	    params.addValue(MomentDao.Field.OWNER_ID.name, obj.getOwnerId());
	    params.addValue(ImageDao.Field.ENABLED.name, obj.getEnabled());
	    params.addValue(MomentDao.Field.CREATED_AT.name, obj.getCreatedAt());
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<Moment> getRowMapper( )
	  {
	    RowMapper<Moment> rm = new RowMapper<Moment>()
	    {
	      @Override
	      public Moment mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  Moment obj = new Moment();
	    	  obj.setId(rs.getInt(MomentDao.Field.ID.name));
	    	  obj.setBody(rs.getString(MomentDao.Field.BODY.name));
	    	  obj.setOwnerId(rs.getInt(MomentDao.Field.OWNER_ID.name));
	    	  obj.setEnabled(rs.getBoolean(ImageDao.Field.ENABLED.name));
	    	  obj.setCreatedAt(rs.getTimestamp(MomentDao.Field.CREATED_AT.name).toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
