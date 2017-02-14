package com.projectning.service.manager.impl;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.UserDao;
import com.projectning.service.dao.UserDetailDao;
import com.projectning.service.dao.impl.NVPair;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.domain.User;
import com.projectning.service.domain.UserDetail;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ErrorMessage;

@Component
public class UserManagerImpl implements UserManager{

	@Autowired UserDao userDao;
	@Autowired UserDetailDao userDetailDao;
	@Autowired HelperManager helperManager;
	
	@Override
	public String registerForSalt(String username, int offset) throws IllegalStateException {
		if(checkUsername(username))
			throw new IllegalStateException(ErrorMessage.USERNAME_UNAVAILABLE.getMsg());
		if (username.length() > 32)
			throw new IllegalStateException(ErrorMessage.USERNAME_TOO_LONG.getMsg());
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(username);
		if(!m.matches())
			throw new IllegalStateException(ErrorMessage.USERNAME_NOT_EMAIL.getMsg());
		
		
		
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
			throw new IllegalStateException(ErrorMessage.USER_INTERN_ERROR.getMsg());
		if(password.length() != 32)
			throw new IllegalStateException(ErrorMessage.USER_INTERN_ERROR.getMsg());
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.PASSWORD.getQueryTerm("password"));
		User user;
		try{
			user = userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_INTERN_ERROR.getMsg());
		}
		
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
		User user;
		try{
			user = userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
		
		NVPair newValue = new NVPair(UserDao.Field.VERI_TOKEN.name, code);
		
		userDao.update(user.getId(), newValue);
	}

	@Override
	public void checkVeriCode(String username, String code) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.VERI_TOKEN.getQueryTerm(code));
		try{
			userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.INVALID_VERIFICATION_CODE.getMsg());
		}
	}

	@Override
	public void confirmEmail(String username) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		User user;
		try{
			user = userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
		
		List<NVPair> newValue = new ArrayList<NVPair>();
		newValue.add(new NVPair("veri_token", ""));
		newValue.add(new NVPair("email_confirmed", true));
		
		userDao.update(user.getId(), newValue);
		
	}

	@Override
	public void updateAccessToken(String username, String token) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		User user;
		try{
			user = userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
		
		NVPair pair = new NVPair(UserDao.Field.AUTH_TOKEN.name, token);
		
		userDao.update(user.getId(), pair);
		
	}

	@Override
	public String loginForSalt(String username) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		try{
			return userDao.findObject(terms).getSalt();
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
	}

	@Override
	public User login(String username, String password, String accessToken) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		terms.add(UserDao.Field.PASSWORD.getQueryTerm(password));
		User user;
		try{
			user = userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_INTERN_ERROR.getMsg());
		}
		
		NVPair pair = new NVPair(UserDao.Field.AUTH_TOKEN.name, accessToken);
		
		userDao.update(user.getId(), pair);
		user.setAuthToken(accessToken);
		return user;
	}

	@Override
	public int getUserId(String username) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username));
		try{
			return userDao.findObject(terms).getId();
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
	}

	@Override
	public void checkUserIdExistance(int id) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.ID.getQueryTerm(id));
		try{
			userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
	}
	
	@Override
	public String getUsername(int userId) throws NotFoundException{
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.ID.getQueryTerm(userId));
		try{
			return userDao.findObject(terms).getUsername();
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMsg());
		}
	}
	
	public int validateAccessToken(Map<String, Object> request) 
			throws NullPointerException, NotFoundException, IllegalStateException{
		String accessToken = (String) request.get("accessToken");
		Map<String, Object> result = helperManager.decodeJWT(accessToken);
		
		helperManager.checkSessionTimeOut((String)result.get("expire"));
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm((String)result.get("username")));
		terms.add(UserDao.Field.AUTH_TOKEN.getQueryTerm(accessToken));
		try{
			return userDao.findObject(terms).getId();
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.INVALID_ACCESS_TOKEN.getMsg());
		}
	}
	
	@Override
	public String getUserDisplayedName(int userId) throws NotFoundException{
		try{
			UserDetail userDetail = getUserDetail(userId);
			if(userDetail.getNickname() != null){
				return userDetail.getNickname();
			}else if(userDetail.getName() != null){
				return userDetail.getName();
			}else{
				throw new NotFoundException();
			}
		}catch(NotFoundException e){
			try{
				return getUsername(userId);
			}catch(NotFoundException ex){
				throw new IllegalStateException(ErrorMessage.USER_NOT_FOUND.getMsg());
			}
		}
	}
	
	/* The following methods are for user details*/

	@Override
	public UserDetail getUserDetail(int userId) throws NotFoundException{
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDetailDao.Field.USER_ID.getQueryTerm(userId));
		try{
			return userDetailDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(ErrorMessage.USER_DETAIL_NOT_FOUND.getMsg());
		}
	}
}

	
