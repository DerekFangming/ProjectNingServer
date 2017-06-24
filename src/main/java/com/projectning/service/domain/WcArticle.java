package com.projectning.service.domain;

import java.time.Instant;

public class WcArticle {
	private int id;
	private String title;
	private String article;
	private int menuId;
	private int userId;
	private Instant createdAt;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getArticle(){
		return article;
	}
	
	public void setArticle(String article){
		this.article = article;
	}
	
	public int getMenuId(){
		return menuId;
	}
	
	public void setMenuId(int menuId){
		this.menuId = menuId;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}

}
