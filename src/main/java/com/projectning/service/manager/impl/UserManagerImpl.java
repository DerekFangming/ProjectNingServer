package com.projectning.service.manager.impl;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.UserDao;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.domain.User;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager{

	@Autowired UserDao userDao;
	
	@Override
	public String registerForSalt(String username) throws IllegalStateException {
		if(checkUsername(username))
			throw new IllegalStateException("Username already exist");
		if (username.length() > 32)
			throw new IllegalStateException("Username exceeds maximum length 32");
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(username);
		if(!m.matches())
			throw new IllegalStateException("Username has to be an email");
		
		
		
		final Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		String encodedSalt = Base64.encodeBase64String(salt).substring(0, 32);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword("password");
		user.setCreatedAt(Instant.now());
		user.setEmailConfirmed(false);
		user.setSalt(encodedSalt);
		userDao.persist(user);
		
		return encodedSalt;
	}
	
	@Override
	public void register(String username, String password) throws IllegalStateException, NotFoundException {
		if(username.length() > 32)
			throw new IllegalStateException("Username internal error");
		if(password.length() != 32)
			throw new IllegalStateException("Password internal error");
		
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(UserDao.Field.USERNAME.getQueryTerm(username));
		values.add(UserDao.Field.PASSWORD.getQueryTerm("password"));
		User user = userDao.findObject(values);
		
		NVPair pair = new NVPair("password", password);
		
		userDao.update(user.getId(), pair);
	}

	@Override
	public boolean checkUsername(String username) {
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(UserDao.Field.USERNAME.getQueryTerm(username));
		return userDao.getCount(values) > 0;
		
	}

	@Override
	public void updateVeriCode(String username, String code) throws NotFoundException{
		List<QueryTerm> values = new ArrayList<QueryTerm>();
		values.add(UserDao.Field.USERNAME.getQueryTerm(username));
		User user = userDao.findObject(values);
		
		NVPair pair = new NVPair("veri_token", code);
		
		userDao.update(user.getId(), pair);
	}

}

	
