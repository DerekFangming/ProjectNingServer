package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.UserDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.User;

@Repository
@Jdbc
public class JdbcUserDao extends JdbcBaseDao<User> implements UserDao{
	public JdbcUserDao()
	  {
	    super(CoreTableType.USERS);
	  }
	
	@Override
	  protected NVPairList getNVPairs(User obj)
	  {
	    NVPairList params = new NVPairList();
	    
	    params.addValue(UserDao.Field.USERNAME.name, obj.getUsername());
	    params.addValue(UserDao.Field.PASSWORD.name, obj.getPassword());
	    params.addValue(UserDao.Field.AUTH_TOKEN.name, obj.getAuthToken() == "" ? null : obj.getAuthToken());
	    params.addValue(UserDao.Field.VERI_TOKEN.name, obj.getVeriToken() == "" ? null : obj.getVeriToken());
	    params.addValue(UserDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	    params.addValue(UserDao.Field.EMAIL_CONFIRMED.name, obj.getEmailConfirmed());
	    params.addValue(UserDao.Field.SALT.name, obj.getSalt());
	    params.addValue(UserDao.Field.TIMEZONE_OFFSET.name, obj.getTimezoneOffset());
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<User> getRowMapper( )
	  {
	    RowMapper<User> rm = new RowMapper<User>()
	    {
	      @Override
	      public User mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  User user = new User();
	    	  user.setId(rs.getInt(UserDao.Field.ID.name));
	    	  user.setUsername(rs.getString(UserDao.Field.USERNAME.name));
	    	  user.setPassword(rs.getString(UserDao.Field.PASSWORD.name));
	    	  user.setAuthToken(rs.getString(UserDao.Field.AUTH_TOKEN.name));
	    	  user.setVeriToken(rs.getString(UserDao.Field.VERI_TOKEN.name));
	    	  user.setCreatedAt(rs.getTimestamp(UserDao.Field.CREATED_AT.name).toInstant());
	    	  user.setEmailConfirmed(rs.getBoolean(UserDao.Field.EMAIL_CONFIRMED.name));
	    	  user.setSalt(rs.getString(UserDao.Field.SALT.name));
	    	  user.setTimezoneOffset(rs.getInt(UserDao.Field.TIMEZONE_OFFSET.name));
	        
	        return user;
	      }
	    };
	    
	    return rm;
	  }

}
