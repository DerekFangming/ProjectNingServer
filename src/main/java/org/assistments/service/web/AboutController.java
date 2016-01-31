package org.assistments.service.web;

import java.util.Map;

import org.assistments.service.manager.AboutManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    
    System.out.println("haha");
    
    return new ResponseEntity<Map<String, Object>>(info, HttpStatus.OK);
  }
}
