package com.projectning.service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.projectning.service.manager.HelperManager;
import com.projectning.service.manager.UserManager;

@Controller
public class UserController {
	
	@Autowired private HelperManager helperManager;
	@Autowired private UserManager userManager;
	

}
