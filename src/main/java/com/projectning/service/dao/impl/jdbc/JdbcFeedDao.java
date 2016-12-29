package com.projectning.service.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.dao.FeedDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.Feed;

@Repository
@Jdbc
public class JdbcFeedDao extends JdbcBaseDao<Feed> implements FeedDao{
	public JdbcFeedDao()
	  {
	    super(CoreTableType.FEEDS);
	  }
	
	@Override
	  protected NVPairList getNVPairs(Feed obj)
	  {
	    NVPairList params = new NVPairList();
	    
		params.addValue(FeedDao.Field.BODY.name, obj.getBody());
	    params.addValue(FeedDao.Field.OWNER_ID.name, obj.getOwnerId());
	    params.addValue(ImageDao.Field.ENABLED.name, obj.getEnabled());
	    params.addValue(FeedDao.Field.CREATED_AT.name, obj.getCreatedAt());
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<Feed> getRowMapper( )
	  {
	    RowMapper<Feed> rm = new RowMapper<Feed>()
	    {
	      @Override
	      public Feed mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  Feed obj = new Feed();
	    	  obj.setId(rs.getInt(FeedDao.Field.ID.name));
	    	  obj.setBody(rs.getString(FeedDao.Field.BODY.name));
	    	  obj.setOwnerId(rs.getInt(FeedDao.Field.OWNER_ID.name));
	    	  obj.setEnabled(rs.getBoolean(ImageDao.Field.ENABLED.name));
	    	  obj.setCreatedAt(rs.getTimestamp(FeedDao.Field.CREATED_AT.name).toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
