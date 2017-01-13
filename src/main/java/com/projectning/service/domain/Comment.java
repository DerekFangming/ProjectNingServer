package com.projectning.service.domain;

import java.time.Instant;

public class Comment {
	private int id;
	private String body;
	private int mentionedUserId;
	private String type;
	private int typeMappingId;
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
	
	public int getMentionedUserId(){
		return mentionedUserId;
	}
	
	public void setMentionedUserId(int mentionedUserId){
		this.mentionedUserId = mentionedUserId;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public int getTypeMappingId(){
		return typeMappingId;
	}
	
	public void setTypeMappingId(int typeMappingId){
		this.typeMappingId = typeMappingId;
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
