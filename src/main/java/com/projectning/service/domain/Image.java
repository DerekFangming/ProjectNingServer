package com.projectning.service.domain;

import java.time.Instant;

public class Image {
	private int id;
	private String location;
	private String type;
	private int typeMappingId;
	private int ownerId;
	private Instant createdAt;
	private boolean enabled;
	private String title;
	private String imageData;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
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
	
	public Instant getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt){
		this.createdAt = createdAt;
	}
	
	public boolean getEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getImageData(){
		return imageData;
	}
	
	public void setImageData(String imageData){
		this.imageData = imageData;
	}

}
