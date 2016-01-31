package org.assistments.service;

import org.assistments.service.manager.AboutManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/pgService.xml")
public class AboutManagerTests extends TestCase
{
	@Autowired private @Qualifier("sampleServiceAboutManager")AboutManager helloMgr;
	
	@Test
	public void helloTest()
	{
		assertTrue(helloMgr.getInfo().get(AboutManager.SDK_VERSION_KEY).equals(AboutManager.SDK_VERSION));
		assertTrue(helloMgr.getInfo().get(AboutManager.RELEASE_DATE_KEY).equals(AboutManager.RELEASE_DATE));
	}
}
