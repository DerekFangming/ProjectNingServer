package org.assistments.service.domain;

import org.assistments.util.ToStringHelper;
import org.assistments.util.Util;

/**
 * An <tt>XPair</tt> contains identifiers used by applications to reference SDK-persisted objects which are exposed
 * to applications.  Those object types are specified by the types included in {@link ExternalReferenceType}. 
 * <p>
 * When an application persists one of those object types, by calling a Manager's <tt>persist()</tt> method, <tt>persist()</tt>
 * returns the object updated with its externally available identifiers. Those identifiers are an:
 * <ul>
 * <li><tt>xid</tt> - an external ID, as a long, specific to the application that persists the object. While expected to be 
 * long-lived the SDK does not guarantee that these values are static over time. Consequently, SDK applications may choose to 
 * use <tt>xid</tt>s in real-time, but they should not be persisted by the application for later use.
 * </li>
 * <li><tt>xref</tt> - an external reference, as a UUID, specific the persisted object. These external references are 
 * guaranteed to be static over time. SDK applications that need to track objects persisted via the SDK should do so
 * using the objects' <tt>xref</tt>s. 
 * </li>
 * </ul>   
 * <p>
 * In most use cases of an <tt>XPair</tt>, SDK methods accept an <tt>XPair</tt> containing either an <tt>xid</tt> or <tt>xref</tt> 
 * (as well as both). The method {@link XPair#getState(XPair)} returns the enum {@link XPairState} to revealing the state of an 
 * <tt>XPair</tt>   
 */
public class XPair implements HasExternalReference
{
  public enum XPairState {
    XID,
    XREF,
    BOTH,
    NONE;
  }
  
  private long xid = 0;
  private String xref = null;
  
  public XPair()
  {
    
  }

  public XPair(long xid)
  {
    this.setXid(xid);
  }

  public XPair(Package caller, String xref)
  {
    this.setXref(caller, xref);
  }

  public XPair(Package caller, long xid, String xref)
  {
    this.setXid(xid);
    this.setXref(caller, xref);
  }

  
  /**
   * @return the xid
   */
  public long getXid( )
  {
    return xid;
  }

  /**
   * @param xid the xid to set
   * @throws IllegalAccessError 
   */
  public void setXid(long xid) 
  {
    if(this.xid != 0)
    {
      throw new IllegalAccessError("Cannot set an xid value more than once. Do you need to copy your object?");
    }
    
    this.xid = xid;
  }

  /**
   * @return the xref
   */
  public String getXref( )
  {
    return xref;
  }

  /**
   * @param xref the xref to set
   * @throws IllegalAccessError 
   */
  // TODO: Verify if really need synchronized: 
  // How is it possible multiple threads would ever be calling this on as single obj?
  public synchronized void setXref(Package caller, String xref) 
  {
    /*
    if the service, can overwrite the value set by the superclass constructor
    If not the service, cannot set more than 1x  
    */
    if(!caller.getName().startsWith("org.assistments.service"))
    {
      if(this.xref != null)
      {
        throw new IllegalAccessError("Cannot set an xref value more than once from: " + caller.getName()
          + Util.NL + "Do you need to copy your object?");
      }
    }

    // Not yet set, or set by the SDK code.
    this.xref = xref;
  }
  
  protected void setXref(String xref)
  {
    this.xref = xref;
  }
  
  @Override
  
  public XPair getXinfo( )
  {
    XPair xinfo = new XPair();
    
    xinfo.setXid(this.getXid());

    // Use private method to override the public set method.
    xinfo.setXref(this.getXref());
    
    return xinfo;    
  }

  @Override
  public void setXinfo(Package caller, XPair xinfo)
  {
    this.setXid(xinfo.getXid());
    this.setXref(caller, xinfo.getXref());
  }

  public static XPairState getState(XPair xinfo)
  {
    XPairState state = XPairState.NONE;

    // If null, returns NONE.
    if (xinfo != null)
    {
      if (xinfo.getXid() != 0)
      {
        state = XPairState.XID;

        if (xinfo.getXref() != null)
        {
          state = XPairState.BOTH;
        }
      }
      else
      {
        if (xinfo.getXref() != null)
        {
          state = XPairState.XREF;
        }
      }
    }
    
    return state;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode( )
  {
    return this.getXref().hashCode();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof XPair))
    {
      return false;
    }
    
    XPair other = (XPair) obj;
    
    // By definition (since the parent object constructor should have generated an xref), neither
    // should be null. If one is by chance...
    if ((xref == null) || (other.getXref() == null))
    {
        return false;
    }
    else if (!xref.equals(other.xref))
    {
      return false;
    }
    
    return true;
  }

  @Override
  public String toString()
  {
    return this.toString("");
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder sb = new StringBuilder();

//    sb = ToStringHelper.variableToString(sb, indent, "type", this.getClass().getName());
    sb = ToStringHelper.variableToString(sb, indent, "xid", this.xid);
    sb = ToStringHelper.variableToString(sb, indent, "xref", this.xref);

    return sb.toString();
  }

  @Override
  public boolean isPersisted( )
  {
    return ((this.xid > 0) && !Util.isNullOrEmpty(this.getXref()));    
  }

  @Override
  public void verifyIsPersisted( )
  {
    this.verifyIsPersisted("Object must be persisted - and it appears not to be.");
  }
  
  @Override
  public void verifyIsPersisted(String msg)
  {
    if(!isPersisted())
    {
      throw new IllegalArgumentException(msg);
    }
  }
}
