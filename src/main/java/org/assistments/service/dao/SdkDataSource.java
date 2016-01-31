package org.assistments.service.dao;

import java.util.List;
import java.util.Map;

public interface SdkDataSource
{
  String getDbNickname();
  
  /**
   * Returns the database's actual name
   */
  String getDbName();
  
  String getServerName();
  
  /**
   * @return the data source type
   */
  DataSourceType getDataSourceType( );

  /**
   * @param dst The type of this data source 
   */
  void setDataSourceType(DataSourceType dst);

  void addTable(SchemaTable table);
  
  SchemaTable getTable(String tableName);
  
  List<String> getTableNames();
  
  List<SchemaTable> getSchemaTables();
  
  Map<String,SchemaTable> getTables();

  String toString();
}
