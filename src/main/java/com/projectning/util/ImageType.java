package com.projectning.util;

public enum ImageType {
	AVATAR("Avatar"),
	COVER_IMG("Cover Image"),
	MOMENT("Moment"),
	OTHERS("Others");
	
	private final String name;
	
	ImageType(String name) {
        this.name = name;
    }
	
	public String getName(){
		return name;
	}
}
