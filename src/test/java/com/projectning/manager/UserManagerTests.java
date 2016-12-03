package com.projectning.manager;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/pnServiceTesting.xml")
public class UserManagerTests {
	
	@Autowired private UserManager userManager;
	
	@Test
	public void testRegister(){
		try {
			userManager.registerForSalt("TestUser@fmning.com", 0);
			fail(ErrorMessage.SHOULD_NOT_PASS_ERROR.getMsg());
		} catch (IllegalStateException e) {
			assertEquals(e.getMessage(), ErrorMessage.USERNAME_UNAVAILABLE.getMsg());
		}
	}
	
	@Test
	public void testLoginForSalt(){
		try {
			assertEquals(userManager.loginForSalt("TestUser@fmning.com"), "testUserSalt");
		} catch (NotFoundException e) {
			fail(e.toString());
		}
		try {
			userManager.loginForSalt("wrongUser");
			fail(ErrorMessage.SHOULD_NOT_PASS_ERROR.getMsg());
		} catch (NotFoundException e) {
			return;
		}
	}
	
	@Test
	public void testLogin(){
		try {
			userManager.login("TestUser@fmning.com", "testUserPassword", "");
		} catch (NotFoundException e) {
			fail(e.toString());
		}
		try{
			userManager.login("TestUser@fmning.com", "WRONG", "");
			fail(ErrorMessage.SHOULD_NOT_PASS_ERROR.getMsg());
		} catch (NotFoundException e){
		}
		try{
			userManager.login("WRONG", "testUserPassword", "");
			fail(ErrorMessage.SHOULD_NOT_PASS_ERROR.getMsg());
		} catch (NotFoundException e){
			return;
		}
	}
	
	@Test
	public void testCheckUsername(){
		assertTrue(userManager.checkUsername("TestUser@fmning.com"));
		assertFalse(userManager.checkUsername("WRONG"));
	}
	
	@Test
	public void testUpdateVerificationCode(){
		try{
			userManager.updateVeriCode("WRONG", "");
		}catch(NotFoundException e){
			assertEquals(e.getMessage(), ErrorMessage.USER_NOT_FOUND.getMsg());
		}
	}
	
	@Test
	public void testCheckVerificationCode(){
		try{
			userManager.checkVeriCode("WRONG","");
			fail(ErrorMessage.SHOULD_NOT_PASS_ERROR.getMsg());
		}catch(NotFoundException e){
		}
		try{
			userManager.checkVeriCode("TestUser@fmning.com","");
		}catch(NotFoundException e){
			fail(e.toString());
		}
	}
	
//	@Test
//	public void createUser()
//	  {
//		final Random r = new SecureRandom();
//		byte[] salt = new byte[32];
//		r.nextBytes(salt);
//		String encodedSalt = Base64.encodeBase64String(salt);
//		assertEquals("", encodedSalt.substring(0, 32));
//	  }

}
