package com.projectning.service.manager.impl;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import org.springframework.stereotype.Component;

import com.projectning.service.manager.HelperManager;

@Component
public class HelperManagerImpl implements HelperManager{

	@Override
	public JsonObject stringToJsonHelper(String jsonStr) throws JsonParsingException {
		JsonReader reader = Json.createReader(new StringReader(jsonStr));
		JsonObject jsonObj = reader.readObject();
		reader.close();
		return jsonObj;
	}

}
