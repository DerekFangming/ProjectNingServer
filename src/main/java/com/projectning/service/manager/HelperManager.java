package com.projectning.service.manager;

import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

public interface HelperManager {

	public JsonObject stringToJsonHelper(String jsonStr) throws JsonParsingException;
	
	public void emailConfirm(String to, String subject, String content);
}
