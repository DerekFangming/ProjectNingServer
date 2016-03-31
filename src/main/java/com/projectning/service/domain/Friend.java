package com.projectning.service.domain;

import java.time.Instant;

public class Friend {
	
	private int id;
	private int senderId;
	private int receiverId;
	private boolean approved;
	private Instant createdAt;
	private Instant approvedAt;
	
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
	
	public boolean getapproved(){
		return approved;
	}
	
	public void setApproved(boolean approved){
		this.approved = approved;
	}
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}
	
	public Instant getApprovedAt(){
		return approvedAt;
	}
	
	public void setApprovedAt(Instant approvedAt){
		this.approvedAt = approvedAt;
	}

}
