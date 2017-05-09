package com.projectning.service.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.projectning.service.dao.SgReportDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.NVPairList;
import com.projectning.service.domain.SgReport;

@Repository
@Jdbc
public class JdbcSgReportDao extends JdbcBaseDao<SgReport> implements SgReportDao{
	public JdbcSgReportDao()
	  {
	    super(CoreTableType.SG);
	  }
	
	@Override
	  protected NVPairList getNVPairs(SgReport obj)
	  {
	    NVPairList params = new NVPairList();
	    params.addValue(SgReportDao.Field.MENU_ID.name, obj.getMenuId());
	    params.addValue(SgReportDao.Field.EMAIL.name, obj.getEmail());
	    params.addValue(SgReportDao.Field.REPORT.name, obj.getReport());
	    params.addValue(SgReportDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
	        
	    return params;
	  }

	  @Override
	  protected RowMapper<SgReport> getRowMapper( )
	  {
	    RowMapper<SgReport> rm = new RowMapper<SgReport>()
	    {
	      @Override
	      public SgReport mapRow(ResultSet rs, int row) throws SQLException
	      {
	    	  SgReport obj = new SgReport();
	    	  obj.setId(rs.getInt(SgReportDao.Field.ID.name));
	    	  obj.setMenuId(rs.getInt(SgReportDao.Field.MENU_ID.name));
	    	  obj.setEmail(rs.getString(SgReportDao.Field.EMAIL.name));
	    	  obj.setReport(rs.getString(SgReportDao.Field.REPORT.name));
	    	  obj.setCreatedAt(rs.getTimestamp(SgReportDao.Field.CREATED_AT.name).toInstant());
	        
	        return obj;
	      }
	    };
	    
	    return rm;
	  }

}
