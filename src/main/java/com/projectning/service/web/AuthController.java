package com.projectning.service.web;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParsingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.auth.JWTSigner;
import com.projectning.auth.JWTVerifier;
import com.projectning.auth.JWTVerifyException;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.UserManager;

@Controller
public class AuthController {
	
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/register_for_salt")
    public ResponseEntity<String> registerForSalt(@RequestBody String request) {
		String salt = "";
		JsonObjectBuilder respond = Json.createObjectBuilder();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			salt = userManager.registerForSalt(jsonObj.getString("username"));
			respond.add("salt", salt);
			respond.add("error", "");
		}catch(JsonParsingException e){
			respond.add("error", "Request format incorrest");
		}catch(IllegalStateException e){
			respond.add("error", e.getMessage());
		}
	
		return new ResponseEntity<String>(respond.build().toString(), HttpStatus.OK);
		
	}
	
	@RequestMapping("/register")
    public ResponseEntity<String> register(@RequestBody String request) {
		JsonObjectBuilder respond = Json.createObjectBuilder();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			String username = jsonObj.getString("username");
			String password = jsonObj.getString("password");
			userManager.register(username, password);
			String code = helperManager.getEmailConfirmCode(username);
			helperManager.emailConfirm(username, code);
			userManager.updateVeriCode(username, code);
			respond.add("status", "ok");
			respond.add("username", username);
			respond.add("emailConfirmed","false");
			respond.add("error", "");
		}catch(JsonParsingException e){
			respond.add("error", "Request format incorrest");
		}catch(NullPointerException e){
			respond.add("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.add("error", e.getMessage());
		}catch(NotFoundException e){
			respond.add("error", "User not found");
		}
	
		return new ResponseEntity<String>(respond.build().toString(), HttpStatus.OK);
		
	}
	
	@RequestMapping("/email/*")
    public ResponseEntity<String> emailVerifivation(HttpServletRequest request) {
		String code = request.getRequestURI().split("/email/")[1];
		System.out.println(code.replace("=", "."));
		
		return new ResponseEntity<String>(helperManager.getPage(), HttpStatus.OK);
	}
	
	@RequestMapping("/auth/*")
    public ResponseEntity<String> home(HttpServletRequest request) {
		//helperManager.emailConfirm("synfm123@gmail.com", "www.google.com");
        //System.out.println(request.getRequestURI());
        
		helperManager.getEmailConfirmCode("synfm123@gmail.com");
        
        String b = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9=eyJleHBpcmUzIjoyMDEwMTAxMCwiZXhwaXJlNCI6MjAxMDEwMTAsImV4cGlyZTIiOjIwMTAxMDEwLCJleHBpcmUiOjIwMTAxMDEwLCJ1c2VybmFtZTQiOiJhZG1pbiIsInVzZXJuYW1lMyI6ImFkbWluIiwidXNlcm5hbWUyIjoiYWRtaW4iLCJ1c2VybmFtZSI6ImFkbWluIn0=2FYRg1Qd035JLNwAOz7MRcZ8iQEuV3ZjpWEmzByiOQ8";

        helperManager.emailConfirm("synfm123@gmail.com", b);
        
        
        
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

}
