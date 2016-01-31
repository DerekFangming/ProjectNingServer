package com.projectning.service.dao.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.DataSourceRegistry;
import com.projectning.service.dao.DataSourceType;
import com.projectning.service.dao.SdkDataSource;

/**
 * CoreDataSourceType specifies the concrete data sources available through the SDK.  
 */
public enum CoreDataSourceType implements DataSourceType
{
  /**
   * Represents the core ASSISTments Service database available through the SDK.
   */
  CORE,
  
  /**
   * Represents the legacy database used primarily through assistments.org.
   */
  LEGACY;

  private SdkDataSource ds;
  static DataSourceRegistry dsr;
  
  CoreDataSourceType() {
  }

  @Override
  public void init(DataSourceRegistry dsr)
  {
    this.ds = dsr.getDataSource(this.toString());
    this.ds.setDataSourceType(this);
  }
  
  @Override
  public SdkDataSource getDataSource()
  {
    return this.ds;
  }

  @Override
  public String getNickname()
  {
    return this.toString();
  }

  public static DataSourceRegistry getDataSourceRegistry()
  {
    return dsr;
  }
  
  @Component
  public static class DSInjector
  { 
    private static String CLASS_NAME = DSInjector.class.getSimpleName();
    
    @Autowired @Qualifier("dataSourceLegacy") SdkDataSourceImpl dsl;
    @Autowired @Qualifier("dataSourceCore") SdkDataSourceImpl dsc;
    @Autowired DataSourceRegistry dsr;
    
    DSInjector()
    {
      System.out.println(CLASS_NAME + " constructor");
    }
    
    @PostConstruct
    void init()
    {
      dsr.initializeDataSources(CoreDataSourceType.class, CoreTableType.class, dsl, dsc);
      System.out.println("database count: " + dsr.getDatabaseCount());
      CoreDataSourceType.dsr = dsr;
    }    
  }  
}
