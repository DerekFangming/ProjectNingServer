package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.ImageDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.Image;

@Repository
@Jdbc
public class JdbcImageDao extends JdbcBaseDao<Image> implements ImageDao{
	public JdbcImageDao()
	  {
	    super(CoreTableType.IMAGES);
	  }
	
	@Override
	  protected NVPairList getNVPairs(Image obj)
	  {
	    NVPairList params = new NVPairList();
	    
	    params.addValue(ImageDao.Field.LOCATION.name, obj.getLocation());
	    params.addValue(ImageDao.Field.OWNER_ID.name, obj.getOwnerId());
	    params.addValue(ImageDao.Field.TYPE.name, obj.getType() == "" ? null : obj.getType());
	    params.addValue(ImageDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	    params.addValue(ImageDao.Field.ENABLED.name, obj.getEnabled());
	    params.addValue(ImageDao.Field.TITLE.name, obj.getTitle());
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<Image> getRowMapper( )
	  {
	    RowMapper<Image> rm = new RowMapper<Image>()
	    {
	      @Override
	      public Image mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  Image obj = new Image();
	    	  obj.setId(rs.getInt(ImageDao.Field.ID.name));
	    	  obj.setLocation(rs.getString(ImageDao.Field.LOCATION.name));
	    	  obj.setType(rs.getString(ImageDao.Field.TYPE.name));
	    	  obj.setOwnerId(rs.getInt(ImageDao.Field.OWNER_ID.name));
	    	  obj.setCreatedAt(rs.getTimestamp(ImageDao.Field.CREATED_AT.name).toInstant());
	    	  obj.setEnabled(rs.getBoolean(ImageDao.Field.ENABLED.name));
	    	  obj.setTitle(rs.getString(ImageDao.Field.TITLE.name));
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
