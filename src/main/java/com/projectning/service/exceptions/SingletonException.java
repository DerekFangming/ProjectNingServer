package com.projectning.service.exceptions;

public class SingletonException extends IllegalStateException
{
  private static final long serialVersionUID = 1L;

  private static final String defaultMsg = 
    " is supposed to be a singleton class. It has already been instantiated.";
  
  public SingletonException(String classType)
  {
    super(classType + defaultMsg);
  }
  
  public SingletonException(String classType, String msg)
  {
    super(msg + " -- " + classType + defaultMsg);
  }

}
