package com.projectning.service.web;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
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

import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.UserManager;

@Controller
public class AuthController {
	
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/register_for_salt")
    public ResponseEntity<Map<String, Object>> registerForSalt(@RequestBody String request) {
		String salt = "";
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			salt = userManager.registerForSalt(jsonObj.getString("username"), 
					jsonObj.getInt("offset"));
			respond.put("salt", salt);
			respond.put("error", "");
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrect");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrect");
		}
	
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody String request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			JsonObject jsonObj = helperManager.stringToJsonHelper(request);
			String username = jsonObj.getString("username");
			String password = jsonObj.getString("password");
			userManager.register(username, password);
			
			String code = helperManager.getEmailConfirmCode(username);
			userManager.updateVeriCode(username, code);
			helperManager.emailConfirm(username, code.replace(".", "="));
			
			Instant exp = Instant.now().plus(Duration.ofDays(1));
			//Convert to ISO8601 formatted string such as 2013-06-25T16:22:52.966Z
			String accessToken = helperManager.createAccessToken(username, exp);
			userManager.updateAccessToken(username, accessToken);
			
			respond.put("username", username);
			respond.put("accessToken", accessToken);
			respond.put("expire", exp.toString());
			respond.put("emailConfirmed","false");
			respond.put("error", "");
		}catch(JsonParsingException e){
			respond.put("error", "Request format incorrest");
		}catch(NullPointerException e){
			respond.put("error", "Request parameters incorrest");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", "User not found");
		}
	
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/email/*")
    public ResponseEntity<String> emailVerifivation(HttpServletRequest request) {
		String code = request.getRequestURI().split("/email/")[1];
		code = code.replace("=", ".");
		String respond = "";
		
		try{
			Map<String, Object> result = helperManager.decodeJWT(code);
			String username = (String)result.get("username");
			userManager.checkVeriCode(username, code);
			Instant expTime = Instant.parse((String) result.get("expire"));
			if(expTime.compareTo(Instant.now()) > 0){
				userManager.confirmEmail(username);
				respond = "success";
			}else{
				code = helperManager.getEmailConfirmCode(username);
				userManager.updateVeriCode(username, code);
				helperManager.emailConfirm(username, code.replace(".", "="));
				respond = "resend";
			}
		}catch(IllegalStateException e){
			e.printStackTrace();
			respond = "Verification code is not valid";
		}catch(DateTimeParseException e){
			e.printStackTrace();
			respond = "Expiration date format incorrect";
		}catch(NotFoundException e){
			e.printStackTrace();
			respond = "User name is not found in database";
		}
		
		return new ResponseEntity<String>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/auth/*")
    public ResponseEntity<String> home(HttpServletRequest request) {
		
        
        
        
        return new ResponseEntity<String>(helperManager.getEmailConfirmedPage("Invalid code"), HttpStatus.OK);
    }

}
