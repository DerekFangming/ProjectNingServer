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
}
