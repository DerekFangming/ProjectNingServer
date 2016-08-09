package com.projectning.service.domain;

import java.time.Instant;

public class Relationship {
	
	private int id;
	private int senderId;
	private int receiverId;
	private boolean accepted;
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
	
	public boolean getAccepted(){
		return accepted;
	}
	
	public void setAccepted(boolean accepted){
		this.accepted = accepted;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}

}
