package org.assistments.service.manager.impl;

import java.util.HashMap;
import java.util.Map;

import org.assistments.service.manager.AboutManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("sampleServiceAboutManager")
@Qualifier("sampleServiceAboutManager")
public class AboutManagerImpl implements AboutManager {

	@Override
	public Map<String, Object> getInfo() 
	{		
	  Map<String,Object> info = new HashMap<String,Object>();
	  
	  info.put(SDK_VERSION_KEY, SDK_VERSION);
	  info.put(RELEASE_DATE_KEY, RELEASE_DATE);
	  info.put(START_TIME_KEY, START_TIME);
	  
	  return info;
	}

}
