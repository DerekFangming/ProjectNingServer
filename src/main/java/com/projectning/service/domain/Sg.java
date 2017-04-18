package com.projectning.service.domain;

import java.time.Instant;

public class Sg {
	private int id;
	private int menuId;
	private String title;
	private String content;
	private Instant createdAt;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getMenuId(){
		return menuId;
	}
	
	public void setMenuId(int menuId){
		this.menuId = menuId;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}

}
