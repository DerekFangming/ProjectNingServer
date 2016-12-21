package com.projectning.service.web;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectning.service.domain.Moment;
import com.projectning.service.exceptions.NotFoundException;
import com.projectning.service.manager.ImageManager;
import com.projectning.service.manager.MomentManager;
import com.projectning.service.manager.UserManager;
import com.projectning.util.ImageType;
import com.projectning.util.Scalr;
import com.projectning.util.Util;

@Controller
public class MomentController {
	
	@Autowired private UserManager userManager;
	@Autowired private MomentManager momentManager;
	@Autowired private ImageManager imageManager;
	
	@RequestMapping("/get_recent_moments")
    public ResponseEntity<Map<String, Object>> getRecentMoments(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try{
			userManager.validateAccessToken(request);
			int userId = (int)request.get("userId");
			int limit = 10;
			try{
				limit = (int)request.get("limit");
			}catch(NullPointerException e){
				//
			}
			Instant checkPoint = Instant.now();
			try{
				String timeStr = (String)request.get("checkPoint");
				checkPoint = Instant.parse(timeStr);
			}catch(NullPointerException e){
				//
			}
			List<Moment> momentList = momentManager.getRecentMomentByDate(userId, checkPoint, limit);
			List<Map<String, Object>> processedMomentList = new ArrayList<Map<String, Object>>();
			
			for(Moment m : momentList){
				Map<String, Object> processedMoment = new HashMap<String, Object>();
				processedMoment.put("momentId", m.getId());
				processedMoment.put("momentBody", m.getBody());
				processedMoment.put("createdAt", m.getCreatedAt().toString());
				try{
					imageManager.getImageIdListByTypeAndMappingId(ImageType.MOMENT.getName(), m.getId(), userId);
					processedMoment.put("hasImage", true);
				}catch(NotFoundException e){
					processedMoment.put("hasImage", false);
				}
				
				processedMomentList.add(processedMoment);
			}
			respond.put("momentList", processedMomentList);
			respond.put("checkPoint", momentList.get(momentList.size() - 1).getCreatedAt().toString());
			respond.put("error", "");
		}catch(Exception e){
			respond = Util.createErrorRespondFromException(e);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping("/get_moment_cover_image")
    public ResponseEntity<Map<String, Object>> getMomentCoverImage(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		//BufferedImage newImage = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);

	    //Graphics2D g = (Graphics2D) newImage.getGraphics();
	    
	    File img = new File("/Volumes/Data/images/15.jpg");
		BufferedImage buffImg;
		try {
			buffImg = ImageIO.read(img);
			buffImg = Scalr.resize(buffImg,  Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
                    60, 60, Scalr.OP_ANTIALIAS);
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
		    try {
		        ImageIO.write(buffImg, "jpg", Base64.getEncoder().wrap(os));
		        respond.put("img", os.toString(StandardCharsets.ISO_8859_1.name()));
		    } catch (final IOException ioe) {
		        //
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	private BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	} 

}
