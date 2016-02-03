package com.projectning.service.manager.impl;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.UserDao;
import com.projectning.service.domain.User;
import com.projectning.service.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager{

	@Autowired UserDao userDao;
	
	@Override
	public void register(String username, String password) throws IllegalStateException {
		if (username.length() > 32)
			throw new IllegalStateException("Username exceeds maximum length 32");
		if(password.length() != 32)
			throw new IllegalStateException("Password format incorrect");
		
		final Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		String encodedSalt = Base64.encodeBase64String(salt).substring(0, 32);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setCreatedAt(Instant.now());
	}

}
