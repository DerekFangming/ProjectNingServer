package com.projectning.service.dao;

import com.projectning.service.dao.impl.CoreDataSourceType;
import com.projectning.service.dao.impl.SdkDataSourceImpl;

public interface DataSourceRegistry 
{
   int getDatabaseCount( );
  
   SdkDataSource putDataSource(SdkDataSource ds);

   SdkDataSource getDataSource(String nickname);

   SdkDataSource getDataSource(CoreDataSourceType dbt);

   String getDbName(SdkDataSourceImpl ds);
   
   <DST extends Enum<DST> & DataSourceType, TT extends Enum<TT> & SchemaTable>
     void initializeDataSources(Class<DST> dst, Class<TT> tt, SdkDataSource... datasources);
}
