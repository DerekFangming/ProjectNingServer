package com.projectning.service.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.UserDetailDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.UserDetail;

@Repository
@Jdbc
public class JdbcUserDetailDao extends JdbcBaseDao<UserDetail> implements UserDetailDao{
	public JdbcUserDetailDao()
	  {
	    super(CoreTableType.USER_DETAILS);
	  }
	
	@Override
	  protected NVPairList getNVPairs(UserDetail obj)
	  {
	    NVPairList params = new NVPairList();
	    
	    params.addValue(UserDetailDao.Field.USER_ID.name, obj.getUserId());
	    params.addValue(UserDetailDao.Field.NAME.name, obj.getName());
	    params.addValue(UserDetailDao.Field.NICKNAME.name, obj.getNickname());
	    params.addNullableIntValue(UserDetailDao.Field.AGE.name, obj.getAge());
	    params.addValue(UserDetailDao.Field.GENDER.name, obj.getGender());
	    params.addValue(UserDetailDao.Field.LOCATION.name, obj.getLocation());
	    params.addValue(UserDetailDao.Field.WHATS_UP.name, obj.getWhatsUp());
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<UserDetail> getRowMapper( )
	  {
	    RowMapper<UserDetail> rm = new RowMapper<UserDetail>()
	    {
	      @Override
	      public UserDetail mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  UserDetail obj = new UserDetail();
	    	  obj.setId(rs.getInt(UserDetailDao.Field.ID.name));
	    	  obj.setUserId(rs.getInt(UserDetailDao.Field.USER_ID.name));
	    	  obj.setName(rs.getString(UserDetailDao.Field.NAME.name));
	    	  obj.setNickname(rs.getString(UserDetailDao.Field.NICKNAME.name));
	    	  obj.setAge(rs.getInt(UserDetailDao.Field.AGE.name));
	    	  obj.setGender(rs.getString(UserDetailDao.Field.GENDER.name));
	    	  obj.setLocation(rs.getString(UserDetailDao.Field.LOCATION.name));
	    	  obj.setWhatsUp(rs.getString(UserDetailDao.Field.WHATS_UP.name));
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
