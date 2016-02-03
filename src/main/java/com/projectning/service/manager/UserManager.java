package com.projectning.service.manager;

public interface UserManager {
	
	public void register(String username, String password) throws IllegalStateException;

}
