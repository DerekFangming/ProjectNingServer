package com.projectning.service.web;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.projectning.auth.JWTSigner;
import com.projectning.auth.JWTVerifier;
import com.projectning.auth.JWTVerifyException;

@Controller
@RequestMapping("/auth/*")
public class AuthController {
	
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> home(HttpServletRequest request) {
		
        System.out.println(request.getRequestURI());
        
        Map authToken = new HashMap<String, String>();
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
			Iterator it = decode.entrySet().iterator();
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
