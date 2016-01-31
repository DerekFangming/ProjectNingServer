package com.projectning.manager;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.UserManager;

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
