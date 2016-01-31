package org.assistments.service.dao;

/**
 * DataSourceType specifies a data source definition. More than likely the concrete implementation 
 * involves a relational database; but it is not a requirement. 
 * <p>
 * While not required, the SDK recommends defining the data sources in your 
 * application (or service) via an enum class (as does the SDK itself).
 */
public interface DataSourceType
{
  /**
   * Sets the data source registry and provides post-constructor setup 
   * which depends on that registry.
   *  
   * @param dsr The data source registry
   */
  void init(DataSourceRegistry dsr);
  
  /**
   * Returns a concrete data source.
   *  
   * @return The data source implementing this type.
   */
  SdkDataSource getDataSource();
  
  /**
   * Returns the data source's "nickname".  This <i>must</i> be the name
   * specified in Spring configuration xml file - case sensitive.
   * @return
   */
  String getNickname();  
}
