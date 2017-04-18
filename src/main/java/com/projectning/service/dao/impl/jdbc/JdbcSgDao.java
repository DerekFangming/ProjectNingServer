package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.SgDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.Sg;

@Repository
@Jdbc
public class JdbcSgDao extends JdbcBaseDao<Sg> implements SgDao{
	public JdbcSgDao()
	  {
	    super(CoreTableType.SG);
	  }
	
	@Override
	  protected NVPairList getNVPairs(Sg obj)
	  {
	    NVPairList params = new NVPairList();
	    params.addValue(SgDao.Field.MENU_ID.name, obj.getMenuId());
	    params.addValue(SgDao.Field.TITLE.name, obj.getTitle());
	    params.addValue(SgDao.Field.CONTENT.name, obj.getContent());
	    params.addValue(SgDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<Sg> getRowMapper( )
	  {
	    RowMapper<Sg> rm = new RowMapper<Sg>()
	    {
	      @Override
	      public Sg mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  Sg obj = new Sg();
	    	  obj.setId(rs.getInt(SgDao.Field.ID.name));
	    	  obj.setMenuId(rs.getInt(SgDao.Field.MENU_ID.name));
	    	  obj.setTitle(rs.getString(SgDao.Field.TITLE.name));
	    	  obj.setContent(rs.getString(SgDao.Field.CONTENT.name));
	    	  obj.setCreatedAt(rs.getTimestamp(SgDao.Field.CREATED_AT.name).toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}