package com.projectning.service.web;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

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
import com.projectning.util.ErrorMessage;

@Controller
public class AuthController {
	
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	
	@RequestMapping("/register_for_salt")
    public ResponseEntity<Map<String, Object>> registerForSalt(@RequestBody Map<String, Object> request) {
		String salt = "";
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			salt = userManager.registerForSalt((String)request.get("username"), 
					(int)request.get("offset"));
			respond.put("salt", salt);
			respond.put("error", "");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}
	
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String username = (String)request.get("username");
			String password = (String)request.get("password");
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
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.USER_NOT_FOUND.getMsg());
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
			respond = e.getMessage();
		}catch(DateTimeParseException e){
			respond = "Expiration date format incorrect";
		}catch(NotFoundException e){
			respond = ErrorMessage.USER_NOT_FOUND.getMsg();
		}
		// Crazy hack to get the page displayed
		return new ResponseEntity<String>(helperManager.getEmailConfirmedPage(respond), HttpStatus.OK);
	}
	
	@RequestMapping("/login_for_salt")
    public ResponseEntity<Map<String, Object>> loginForSalt(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String username = (String)request.get("username");
			respond.put("salt", userManager.loginForSalt(username));
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.USER_NOT_FOUND.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String username = (String)request.get("username");
			String password = (String)request.get("password");
			
			Instant exp = Instant.now().plus(Duration.ofDays(1));
			String accessToken = helperManager.createAccessToken(username, exp);
			
			userManager.login(username, password, accessToken);
			
			respond.put("username", username);
			respond.put("accessToken", accessToken);
			respond.put("expire", exp.toString());
			respond.put("emailConfirmed","false");
			respond.put("error", "");
		}catch(NullPointerException e){
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		}catch(NotFoundException e){
			respond.put("error", ErrorMessage.USER_NOT_FOUND.getMsg());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping("/auth/*")
    public ResponseEntity<String> home(HttpServletRequest request) {
		
        
        
        
        return new ResponseEntity<String>(helperManager.getEmailConfirmedPage("Invalid code"), HttpStatus.OK);
    }
	
	@RequestMapping("/test")
	public ResponseEntity<String> test(HttpServletRequest request) {
		String a = "haha";
		
		return new ResponseEntity<String>(a + a.toUpperCase() + a, HttpStatus.OK);
	}

}
