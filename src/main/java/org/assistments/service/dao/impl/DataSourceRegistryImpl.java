package org.assistments.service.dao.impl;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.assistments.service.dao.DataSourceRegistry;
import org.assistments.service.dao.DataSourceType;
import org.assistments.service.dao.SchemaTable;
import org.assistments.service.dao.SdkDataSource;

public class DataSourceRegistryImpl implements DataSourceRegistry
{
  Map<String, SdkDataSource> databases = new HashMap<String, SdkDataSource>();
   
  public int getDatabaseCount( )
  {
    return databases.size();
  }
  
  public SdkDataSource putDataSource(SdkDataSource ds)
  {
    String nickname = ds.getDbNickname();
    
    return databases.put(nickname, ds);
  }

  public SdkDataSource getDataSource(String nickname)
  {
    return databases.get(nickname);
  }

  public SdkDataSource getDataSource(CoreDataSourceType dbt)
  {
    return databases.get(dbt.toString());
  }

  public String getDbName(SdkDataSourceImpl ds)
  {
    String url = ds.getUrl();

    String dbName = url.substring(url.lastIndexOf('/') + 1);

    return dbName;
  }

  public <DST extends Enum<DST> & DataSourceType, TT extends Enum<TT> & SchemaTable>
  void initializeDataSources(Class<DST> dst, Class<TT> tt, SdkDataSource... datasources)
  {
    for(SdkDataSource ds : datasources)
    {
      this.putDataSource(ds);
    }
    
    for(DST type : EnumSet.allOf(dst))
    {
//      type.setDSR(this);
      type.init(this);
    }
    
    for(TT type : EnumSet.allOf(tt))
    {
//      type.setDSR(this);
      type.init(this);
    }    
  }

}
