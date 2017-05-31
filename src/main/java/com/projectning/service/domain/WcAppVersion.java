package com.projectning.service.domain;

public class WcAppVersion {
	private int id;
	String appVersion;
	String status;
	String title;
	String message;
	String updates;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getAppVersion(){
		return appVersion;
	}
	
	public void setAppVersion(String appVersion){
		this.appVersion = appVersion;
	}

	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getUpdates(){
		return updates;
	}
	
	public void setUpdates(String updates){
		this.updates = updates;
	}

}
