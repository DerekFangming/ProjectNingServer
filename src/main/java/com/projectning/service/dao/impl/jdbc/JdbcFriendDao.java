package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.projectning.service.dao.FriendDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.Friend;

public class JdbcFriendDao extends JdbcBaseDao<Friend> implements FriendDao{
	public JdbcFriendDao()
	  {
	    super(CoreTableType.FRIENDS);
	  }
	
	@Override
	  protected NVPairList getNVPairs(Friend obj)
	  {
	    NVPairList params = new NVPairList();
	    
	    params.addValue(FriendDao.Field.SENDER_ID.name, obj.getSenderId());
	    params.addValue(FriendDao.Field.RECEIVER_ID.name, obj.getReceiverId());
	    params.addValue(FriendDao.Field.APPROVED.name, obj.getapproved());
	    params.addValue(FriendDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	    params.addValue(FriendDao.Field.APPROVED_AT.name, Date.from(obj.getApprovedAt()));
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<Friend> getRowMapper( )
	  {
	    RowMapper<Friend> rm = new RowMapper<Friend>()
	    {
	      @Override
	      public Friend mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  Timestamp approvedTime = rs.getTimestamp((FriendDao.Field.APPROVED_AT.name));
	    	  
	    	  Friend obj = new Friend();
	    	  obj.setId(rs.getInt(FriendDao.Field.ID.name));
	    	  obj.setSenderId(rs.getInt(FriendDao.Field.SENDER_ID.name));
	    	  obj.setReceiverId(rs.getInt(FriendDao.Field.RECEIVER_ID.name));
	    	  obj.setApproved(rs.getBoolean(FriendDao.Field.APPROVED.name));
	    	  obj.setCreatedAt(rs.getTimestamp(FriendDao.Field.CREATED_AT.name).toInstant());
	    	  obj.setApprovedAt(approvedTime == null ? null : approvedTime.toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
