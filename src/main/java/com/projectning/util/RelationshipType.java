package com.projectning.util;

public enum RelationshipType {
	FRIEND("Friend"),
	DENIED("Denied"),
	FRIEND_CONFIRMED("FC"),
	FRIEND_REQUESTED("FR");
	
	private final String name;
	
	RelationshipType(String name) {
        this.name = name;
    }
	
	public String getName(){
		return name;
	}
}
