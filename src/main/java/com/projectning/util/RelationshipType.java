package com.projectning.util;

public enum RelationshipType {
	FRIEND("Friend"),
	DENIED("Denied");
	
	private final String name;
	
	RelationshipType(String name) {
        this.name = name;
    }
	
	public String getName(){
		return name;
	}
}
