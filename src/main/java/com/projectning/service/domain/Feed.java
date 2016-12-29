package com.projectning.service.domain;

import java.time.Instant;

public class Feed {
	private int id;
	private String body;
	private int ownerId;
	private boolean enabled;
	private Instant createdAt;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getBody(){
		return body;
	}
	
	public void setBody(String body){
		this.body = body;
	}
	
	public int getOwnerId(){
		return ownerId;
	}
	
	public void setOwnerId(int ownerId){
		this.ownerId = ownerId;
	}
	
	public boolean getEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}

}
