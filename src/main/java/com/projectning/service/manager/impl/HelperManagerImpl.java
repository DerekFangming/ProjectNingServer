package com.projectning.service.manager.impl;

import java.io.StringReader;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import com.projectning.service.manager.HelperManager;

@Component
public class HelperManagerImpl implements HelperManager{

	@Override
	public JsonObject stringToJsonHelper(String jsonStr) throws JsonParsingException {
		JsonReader reader = Json.createReader(new StringReader(jsonStr));
		JsonObject jsonObj = reader.readObject();
		reader.close();
		return jsonObj;
	}

	@Override
	public void emailConfirm(String to, String code) {
		String message = "Hi,";
		message += "\n";
		message += "Thank you for registration. Please click on the following link to confirm your email address.";
		message += "<br />";
		message += code;
		message += "<br />";
		message += "Thank you.";
		message += "<br />";
		message += "Project Ning";
		sendEmail("no-reply@fmning.com", to, "ProjectNing Email Confirmation", message);
	}
	
	@Override
	public void sendEmail(String from, String to, String subject, String content){

	      String host = "localhost";

	      Properties properties = System.getProperties();

	      properties.setProperty("mail.smtp.host", host);

	      Session session = Session.getDefaultInstance(properties);

	      try{
	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(from));

	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         message.setSubject(subject);

	         message.setText(content);

	         Transport.send(message);
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}

}
