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
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager{

	@Autowired UserDao userDao;
	@Autowired HelperManager helperManager;
	
	@Override
	public String registerForSalt(String username, int offset) throws IllegalStateException {
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
		user.setTimezoneOffset(offset);
		userDao.persist(user);
		
		return encodedSalt;
	}
	
	@Override
	public void register(String username, String password) throws IllegalStateException, NotFoundException {
		if(username.length() > 32)
			throw new IllegalStateException("Username internal error");
		if(password.length() != 32)
			throw new IllegalStateException("Password internal error");
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.PASSWORD.getQueryTerm("password"));
		User user = userDao.findObject(terms);
		
		NVPair newValue = new NVPair(UserDao.Field.PASSWORD.name, password);
		
		userDao.update(user.getId(), newValue);
	}

	@Override
	public boolean checkUsername(String username) {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		return userDao.exists(terms);
		
	}

	@Override
	public void updateVeriCode(String username, String code) throws NotFoundException{
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		User user = userDao.findObject(terms);
		
		NVPair newValue = new NVPair(UserDao.Field.VERI_TOKEN.name, code);
		
		userDao.update(user.getId(), newValue);
	}

	@Override
	public void checkVeriCode(String username, String code) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.VERI_TOKEN.getQueryTerm(code));
		userDao.findObject(terms);
		
	}

	@Override
	public void confirmEmail(String username) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		User user = userDao.findObject(terms);
		
		List<NVPair> newValue = new ArrayList<NVPair>();
		newValue.add(new NVPair("veri_token", ""));
		newValue.add(new NVPair("email_confirmed", true));
		
		userDao.update(user.getId(), newValue);
		
	}

	@Override
	public void updateAccessToken(String username, String token) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		User user = userDao.findObject(terms);
		
		NVPair pair = new NVPair(UserDao.Field.AUTH_TOKEN.name, token);
		
		userDao.update(user.getId(), pair);
		
	}

	@Override
	public String loginForSalt(String username) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		return userDao.findObject(terms).getSalt();
	}

	@Override
	public void login(String username, String password, String accessToken) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.PASSWORD.getQueryTerm(password));
		User user = userDao.findObject(terms);
		
		NVPair pair = new NVPair(UserDao.Field.AUTH_TOKEN.name, accessToken);
		
		userDao.update(user.getId(), pair);
		
	}

	@Override
	public int getUserId(String username, String accessToken) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.AUTH_TOKEN.getQueryTerm(accessToken));
		return userDao.findObject(terms).getId();
	}

	@Override
	public void checkUserIdExistance(int id) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.ID.getQueryTerm(id));
		userDao.findObject(terms);
		
	}
	

}

	
