package com.projectning.service.manager.impl;

import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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

import com.projectning.auth.JWTSigner;
import com.projectning.service.manager.HelperManager;

@Component
public class HelperManagerImpl implements HelperManager{

	public static final String SECRET = "PJNing";

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
		message += "Thank you for registration Project Ning. Please click on the following link to confirm your email address.";
		message += "\n\n";
		message += code;
		message += "\n\n";
		message += "Thank you.";
		message += "\n";
		message += "ProjectNing Team";
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

	@Override
	public String getEmailConfirmCode(String username) {
		String exp = Instant.now().plus(Duration.ofDays(1)).toString();
		Map<String, Object> authToken = new HashMap<String, Object>();
		authToken.put("username", "username");
		authToken.put("action", "emailVeri");
		authToken.put("expire", exp);
		JWTSigner signer = new JWTSigner(SECRET);
		return signer.sign(authToken);
	}

}
