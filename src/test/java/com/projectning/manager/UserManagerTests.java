package com.projectning.manager;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.UserManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/pnServiceTesting.xml")
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
		final Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		String encodedSalt = Base64.encodeBase64String(salt);
		assertEquals("", encodedSalt.substring(0, 32));
	  }

}
