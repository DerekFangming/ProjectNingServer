package com.projectning.service.dao.impl.jdbc;

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
	    	  user.setId(rs.getLong(UserDao.Field.ID.name));
	    	  user.setUsername(rs.getString(UserDao.Field.USERNAME.name));
	    	  user.setPassword(rs.getString(UserDao.Field.PASSWORD.name));
	        
	        return user;
	      }
	    };
	    
	    return rm;
	  }

}
