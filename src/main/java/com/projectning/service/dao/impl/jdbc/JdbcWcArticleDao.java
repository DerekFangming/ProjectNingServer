package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.WcArticleDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.WcArticle;

@Repository
@Jdbc
public class JdbcWcArticleDao extends JdbcBaseDao<WcArticle> implements WcArticleDao{
	public JdbcWcArticleDao()
	  {
	    super(CoreTableType.WC_ARTICLES);
	  }
	
	@Override
	  protected NVPairList getNVPairs(WcArticle obj)
	  {
	    NVPairList params = new NVPairList();
	    params.addValue(WcArticleDao.Field.TITLE.name, obj.getTitle());
	    params.addValue(WcArticleDao.Field.ARTICLE.name, obj.getArticle());
	    params.addValue(WcArticleDao.Field.MENU_ID.name, obj.getMenuId());
	    params.addValue(WcArticleDao.Field.USER_ID.name, obj.getUserId());
	    params.addValue(WcArticleDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<WcArticle> getRowMapper( )
	  {
	    RowMapper<WcArticle> rm = new RowMapper<WcArticle>()
	    {
	      @Override
	      public WcArticle mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  WcArticle obj = new WcArticle();
	    	  obj.setId(rs.getInt(WcArticleDao.Field.ID.name));
	    	  obj.setTitle(rs.getString(WcArticleDao.Field.TITLE.name));
	    	  obj.setArticle(rs.getString(WcArticleDao.Field.ARTICLE.name));
	    	  obj.setMenuId(rs.getInt(WcArticleDao.Field.MENU_ID.name));
	    	  obj.setUserId(rs.getInt(WcArticleDao.Field.USER_ID.name));
	    	  obj.setCreatedAt(rs.getTimestamp(WcArticleDao.Field.CREATED_AT.name).toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
