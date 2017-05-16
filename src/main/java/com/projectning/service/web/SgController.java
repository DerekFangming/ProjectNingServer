package com.projectning.service.web;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.projectning.service.dao.SgDao;
import com.projectning.service.dao.SgReportDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Sg;
import com.projectning.service.domain.SgReport;
import com.projectning.service.exceptions.NotFoundException;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class SgController {
	@Autowired private SgDao sgDao;
	@Autowired private SgReportDao sgReportDao;
	
	public static String CURRENT_VERSION = "1.00";
	@SuppressWarnings("serial")
	public static Map<String, String> versionInfo = new HashMap<String, String>(){
		{
			put("0.00", "AppUpdate&App out of date&Please go to app store and download the newest version");
			put(CURRENT_VERSION, "Ok& & ");
		}
	};
	
	
	@RequestMapping(value = "/get_sg", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getSg(HttpServletRequest request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int menuId = Integer.parseInt(request.getParameter("menuId"));
			
			QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.SG, QueryType.FIND);
		    qb.addFirstQueryExpression(new QueryTerm(SgDao.Field.MENU_ID.name, menuId));
		    qb.setOrdering(SgDao.Field.CREATED_AT.name, ResultsOrderType.DESCENDING);
		    qb.setLimit(1);
		    Sg sg = sgDao.findAllObjects(qb.createQuery()).get(0);
		    respond.put("title", sg.getTitle());
		    respond.put("content", sg.getContent());
		    respond.put("error", "");
		}catch(NumberFormatException e){
			respond.put("error", "Incorrect request format. Please use menuId as key and put number only as value");
		}catch(NotFoundException e){
			respond.put("error", "Article does not exist");
		}catch(Exception e){
			respond.put("error", e.getStackTrace());
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/add_sg", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSg(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int menuId = (Integer)request.get("menuId");
			String title = (String)request.get("title");
			String content = (String)request.get("content");
			
			if (title == null || content == null) throw new IllegalStateException("Need a valid title and content");
			
			Sg sg = new Sg();
			sg.setMenuId(menuId);
			sg.setTitle(title);
			sg.setContent(content);
			sg.setCreatedAt(Instant.now());
			sgDao.persist(sg);
			respond.put("error", "");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(Exception e){
			respond.put("error", e.getMessage());
		}
	
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/get_version_info", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getVersionInfo(HttpServletRequest request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			String menuId = request.getParameter("version");
			String versionStr = versionInfo.get(menuId);
			String[] values = versionStr.split("&");
			
		    respond.put("status", values[0]);
		    respond.put("title", values[1]);
		    respond.put("msg", values[2]);
		    respond.put("version", CURRENT_VERSION);
		    respond.put("error", "");
		}catch(NumberFormatException e){
			respond.put("error", "Incorrect request format. Please use menuId as key and put number only as value");
		}catch(NotFoundException e){
			respond.put("error", "Article does not exist");
		}catch(Exception e){
			respond.put("error", "Unknown error");
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/add_sg_report", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSgReport(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int menuId = (Integer)request.get("menuId");
			String email = (String)request.get("email");
			String report = (String)request.get("report");
			
			if (email == null || report == null) throw new IllegalStateException("Need a valid email or report");
			if (email.length()>50 || report.length()>500) throw new IllegalStateException("Input too long");
			
			SgReport sgReport = new SgReport();
			sgReport.setMenuId(menuId);
			sgReport.setEmail(email);
			sgReport.setReport(report);
			sgReport.setCreatedAt(Instant.now());
			sgReportDao.persist(sgReport);
			respond.put("error", "");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(Exception e){
			respond.put("error", e.getMessage());
		}
	
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}

}
