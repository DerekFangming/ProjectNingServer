package com.projectning.service.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.projectning.service.manager.AboutManager;

@Controller
@RequestMapping("/about")
public class AboutController
{
  @Autowired private AboutManager sdkAboutMgr;
  
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> getAboutInfo()
  {
    Map<String, Object> info = sdkAboutMgr.getInfo();

    info.put("appVersion", "Project Ning Server Ver 0.1");
    
    return new ResponseEntity<Map<String, Object>>(info, HttpStatus.OK);
  }
}
