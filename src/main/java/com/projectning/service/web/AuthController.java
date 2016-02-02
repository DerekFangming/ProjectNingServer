package com.projectning.service.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth/*")
public class AuthController {
	
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> home(HttpServletRequest request) {

        System.out.println(request.getRequestURI());

        return new ResponseEntity<String>("sucess", HttpStatus.OK);
    }

}
