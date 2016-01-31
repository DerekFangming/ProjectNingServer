package com.projectning.service.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projectning.service.dao.UserDao;
import com.projectning.service.domain.User;
import com.projectning.service.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager{

	@Autowired UserDao userDao;
	
	@Override
	public long adduser() {
		User newUser = new User();
		newUser.setUsername("Fangming");
		newUser.setPassword("ning");
		return userDao.persist(newUser);
		
	}

}
