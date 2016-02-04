package com.projectning.service.manager;

import com.projectning.service.exceptions.NotFoundException;

public interface UserManager {
	
	public String registerForSalt(String username) throws IllegalStateException ;
	
	public void register(String username, String password) throws IllegalStateException, NotFoundException;
	
	public boolean checkUsername(String username);

}
