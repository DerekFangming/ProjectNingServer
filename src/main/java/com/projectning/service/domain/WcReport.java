package com.projectning.service.domain;

import java.time.Instant;

public class WcReport {
	private int id;
	private int userId;
	private int menuId;
	private String email;
	private String report;
	private Instant createdAt;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public int getMenuId(){
		return menuId;
	}
	
	public void setMenuId(int menuId){
		this.menuId = menuId;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getReport(){
		return report;
	}
	
	public void setReport(String report){
		this.report = report;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}

}
