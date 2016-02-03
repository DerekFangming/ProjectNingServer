package com.projectning.service.domain;

import java.time.Instant;

public class User {
	private long id;
	private String username;
	private String password;
	private String authToken;
	private String veriToken;
	private Instant createdAt;
	private boolean emailConfirmed;
	private String salt;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getAuthToken(){
		return authToken;
	}
	
	public void setAuthToken(String authToken){
		this.authToken = authToken;
	}
	
	public String getVeriToken(){
		return veriToken;
	}
	
	public void setVeriToken(String veriToken){
		this.veriToken = veriToken;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}
	
	public boolean getEmailConfirmed(){
		return emailConfirmed;
	}
	
	public void setEmailConfirmed(boolean emailConfirmed){
		this.emailConfirmed = emailConfirmed;
	}
	
	public String getSalt(){
		return salt;
	}
	
	public void setSalt(String salt){
		this.salt = salt;
	}
	
}
