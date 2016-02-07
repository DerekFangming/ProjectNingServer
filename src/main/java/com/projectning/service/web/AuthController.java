package com.projectning.service.web;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
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
		}catch(JsonParsingException e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(IllegalStateException e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<String>(salt, HttpStatus.OK);
		
	}
	
	@RequestMapping("/register")
    public ResponseEntity<String> register(@RequestBody String request) {
		String username = "";
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			username = userManager.registerForSalt(jsonObj.getString("username"));
		}catch(JsonParsingException e){
			
		}
	
		return new ResponseEntity<String>(username, HttpStatus.OK);
		
	}
	
	@RequestMapping("/auth/*")
    public ResponseEntity<String> home(HttpServletRequest request) {
		
        System.out.println(request.getRequestURI());
        
        Map<String, Object> authToken = new HashMap<String, Object>();
        authToken.put("username", "admin");
        authToken.put("expire", 20101010);
        authToken.put("username2", "admin");
        authToken.put("expire2", 20101010);
        authToken.put("username3", "admin");
        authToken.put("expire3", 20101010);
        authToken.put("username4", "admin");
        authToken.put("expire4", 20101010);
        
        JWTSigner signer = new JWTSigner("ProjectNing");
        
        String a = signer.sign(authToken);
        
        String b = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHBpcmUzIjoyMDEwMTAxMCwiZXhwaXJlNCI6MjAxMDEwMTAsImV4cGlyZTIiOjIwMTAxMDEwLCJleHBpcmUiOjIwMTAxMDEwLCJ1c2VybmFtZTQiOiJhZG1pbiIsInVzZXJuYW1lMyI6ImFkbWluIiwidXNlcm5hbWUyIjoiYWRtaW4iLCJ1c2VybmFtZSI6ImFkbWluIn0.2FYRg1Qd035JLNwAOz7MRcZ8iQEuV3ZjpWEmzByiOQ8";

        JWTVerifier verifier = new JWTVerifier("ProjectNing");
        
        Map decode;
        
        try {
			decode = verifier.verify(b);
			Iterator<?> it = decode.entrySet().iterator();
			while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
				| IOException | JWTVerifyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return new ResponseEntity<String>(a, HttpStatus.OK);
    }

}
