package org.assistments.service.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.assistments.service.dao.DataSourceType;
import org.assistments.service.dao.SchemaTable;
import org.assistments.service.dao.SdkDataSource;
import org.assistments.util.ToStringHelper;
import org.assistments.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SdkDataSourceImpl extends DriverManagerDataSource implements SdkDataSource
{
  private String dbNnickname;
  private String dbName;
  private String serverName;
  
  private DataSourceType dst;
  
  // key = table name, value = SchemaTable
  // Important: The LinkedHashMap preserves the order in which items are added.
  // That matters so that table creation happens in the order specified in CoreTableType.
  // That order matters because of table dependencies (such as some Type table has a reference
  // to some other table).
  private Map<String,SchemaTable> tables = new LinkedHashMap<String,SchemaTable>();
  
  public SdkDataSourceImpl()
  {
    super();
  }
  
  @PostConstruct
  public void init()
  {
    String url = this.getUrl();
    int lastSlashPos = url.lastIndexOf('/');
    
    this.dbName = url.substring(lastSlashPos + 1);
    
    this.serverName = url.substring(url.indexOf("://") + 3, lastSlashPos);    
  }

  // nickname param comes from our spring config (xml) file.
  @Autowired
  public void setDbNickname(String dbNickname)
  {
    this.dbNnickname = dbNickname;
  }
  
  public String getDbNickname()
  {
    return this.dbNnickname;
  }
  
  /**
   * Returns the database's actual name
   */
  public String getDbName()
  {
    return this.dbName;
  }
  
  public String getServerName()
  {
    return this.serverName;
  }

  /**
   * @return the data source type
   */
  public DataSourceType getDataSourceType( )
  {
    return dst;
  }

  /**
   * @param dst The type of this data source 
   */
  public void setDataSourceType(DataSourceType dst)
  {
    this.dst = dst;
  }

  public void addTable(SchemaTable table)
  {
    tables.put(table.getTableName(), table);
  }
  
  public SchemaTable getTable(String tableName)
  {
    return tables.get(tableName);
  }
  
  public List<String> getTableNames()
  {
    return new ArrayList<String>(tables.keySet());
  }
  
  public List<SchemaTable> getSchemaTables()
  {
    return new ArrayList<SchemaTable>(tables.values());
  }
  
  public Map<String,SchemaTable> getTables()
  {
    return this.tables;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(Util.NL);
    sb = ToStringHelper.variableToString(sb, ToStringHelper.INDENT_NO, "dbNickname", this.dbNnickname);
    sb = ToStringHelper.variableToString(sb, ToStringHelper.INDENT_NO, "dataSourceType", this.dst.toString());
    sb = ToStringHelper.variableToString(sb, ToStringHelper.INDENT_NO, "dbName", this.dbName);
    sb = ToStringHelper.variableToString(sb, ToStringHelper.INDENT_NO, "serverName", this.serverName);
    sb = ToStringHelper.variableToString(sb, ToStringHelper.INDENT_NO, "tables", this.tables.toString());
    
    return sb.toString();
  }
}
