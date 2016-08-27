package com.projectning.service.domain;

import java.time.Instant;

public class Relationship {
	
	private int id;
	private int senderId;
	private int receiverId;
	private boolean confirmed;
	private String type;
	private Instant createdAt;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getSenderId(){
		return senderId;
	}
	
	public void setSenderId(int senderId){
		this.senderId = senderId;
	}
	
	public int getReceiverId(){
		return receiverId;
	}
	
	public void setReceiverId(int receiverId){
		this.receiverId = receiverId;
	}
	
	public boolean getConfirmed(){
		return confirmed;
	}
	
	public void setConfirmed(boolean confirmed){
		this.confirmed = confirmed;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}

}
