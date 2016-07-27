package com.projectning.util;

public enum ImageType {
	AVATAR("Avatar"),
	OTHERS("Others");
	
	private final String name;
	
	ImageType(String name) {
        this.name = name;
    }
	
	String getName(){
		return name;
	}
}
