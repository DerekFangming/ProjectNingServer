package org.assistments.service.domain;

public interface HasExternalReference
{
  /**
   * Returns a new {@link XPair} instance containing the values returned from calling 
   * {@link #getXid()} and  {@link #getXref()} on the implementing class.
   * 
   * @return A new <tt>XPair</tt> instance.
   */
  XPair getXinfo();
  
  /**
   * Sets the 
   * @param xid
   */
  void setXid(long xid);
  
  long getXid( );
  
  void setXref(Package caller, String xref);
  
  String getXref();
  
  void setXinfo(Package caller, XPair xinfo);

  String toString();
  
  String toString(String indent);
  
  /** 
   * Returns an External Reference's hash code based solely on the object's xref UUID. 
   */
  int hashCode();

  /** 
   * Returns whether two <tt>XPair</tt>s are equal based solely on the object's xref UUID. 
   */
  boolean equals(Object obj);
  
  /**
   * Returns whether this object has been persisted.
   * <p>
   * An object is deemed persisted if and only if:
   * <ul>
   * <li>{@link #getXid()} returns a non-zero value, and</li>
   * <li>{@link #getXref()} returns a non-null, non-empty value</li>
   * </ul>
   * </p>   
   */
  boolean isPersisted();
  
  void verifyIsPersisted( );

  void verifyIsPersisted(String msg);
}
