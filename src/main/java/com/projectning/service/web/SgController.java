package com.projectning.service.web;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.projectning.service.dao.SgDao;
import com.projectning.service.dao.impl.CoreTableType;
import com.projectning.service.dao.impl.QueryBuilder;
import com.projectning.service.dao.impl.QueryTerm;
import com.projectning.service.dao.impl.QueryType;
import com.projectning.service.dao.impl.ResultsOrderType;
import com.projectning.service.domain.Sg;
import com.projectning.service.exceptions.NotFoundException;

@Controller
public class SgController {
	@Autowired private SgDao sgDao;
	
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
	
	@RequestMapping("/add_sg")
    public ResponseEntity<Map<String, Object>> addSg(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			int menuId = (Integer)request.get("menuId");
			String title = (String)request.get("title");
			String content = (String)request.get("content");
			
			if (title == null || content == null) throw new IllegalStateException("Need a valid title and content");
			
			Sg sg = new Sg();
			sg.setMenuId(menuId);
			sg.setTitle((String)request.get("title"));
			sg.setContent((String)request.get("content"));
			sg.setCreatedAt(Instant.now());
			//sgDao.persist(sg);
			respond.put("error", "");
		}catch(IllegalStateException e){
			respond.put("error", e.getMessage());
		}catch(Exception e){
			respond.put("error", e.getMessage());
		}
	
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
		
	}

}
