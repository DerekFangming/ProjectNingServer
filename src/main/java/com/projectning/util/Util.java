package com.projectning.util;

public class Util {
	public final static String imagePath = "/Volumes/Data/images/";
	

	public static String verifyImageType(String type) {
		if (type == null)
			return ImageType.OTHERS.getName();
		
		for (ImageType it : ImageType.values()) {
	        if (it.getName().toUpperCase().equals(type.toUpperCase())) {
	            return type;
	        }
	    }
		
		return ImageType.OTHERS.getName();
    }
	
	public static String nullToEmptyString(String input){
		if(input == null){
			return "";
		}else{
			return input;
		}
	}
	
	public static String emptyStringToNull(String input){
		if(input.trim().equals("")){
			return null;
		}else{
			return input.trim();
		}
	}
}
