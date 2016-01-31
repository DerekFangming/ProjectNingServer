package com.projectning.service.exceptions;

@SuppressWarnings("serial")
public class AlreadyExistsException extends SdkException 
{
  public AlreadyExistsException(String message)
  {
    super(message);
  }

}
