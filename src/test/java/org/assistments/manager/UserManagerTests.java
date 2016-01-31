package org.assistments.manager;

import static org.junit.Assert.*;

import org.assistments.service.exceptions.NotFoundException;
import org.assistments.service.manager.UserManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/sdkServiceTesting.xml")
public class UserManagerTests {
	@Autowired private UserManager userManager;
	
//	@Test
//	public void testFindSequenceBySequenceId()
//	  {
//		 try {
//			 
//		} catch (NotFoundException e) {
//			fail(e.toString());
//		}
//	  }
	
	@Test
	public void createUser()
	  {
		 userManager.adduser();
		 assertEquals(1, 1);
	  }

}
