package org.assistments.service.manager.impl;

import org.assistments.service.dao.UserDao;
import org.assistments.service.domain.User;
import org.assistments.service.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
