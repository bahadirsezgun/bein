package com.beinplanner.contollers.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.mail.MailObj;
import tr.com.beinplanner.mail.MailSenderThread;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/bein/mail")
public class SendMailController {

	@Autowired
	MailSenderThread mailSenderThread;
	
	@Autowired
	LoginSession loginSession;
	
	

	@PostMapping(value="/sendPlanningMail")
	public  @ResponseBody HmiResultObj sendPlanningMail(@RequestBody MailObj mailObj ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		mailObj.setToWho(new String[] {mailObj.getToPerson()});
		try {
			MimeMultipart content = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			  
			mainPart.setText(mailObj.getContent(),"UTF-8", "plain");
			mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
			content.addBodyPart(mainPart);
   
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent( mailObj.getHtmlContent(), "text/html; charset=utf-8" );
			content.addBodyPart(htmlPart);
			
			mailObj.setToFrom(loginSession.getUser().getUserEmail());
			
			mailObj.setMultipartMessage(content);
			hmiResultObj=mailSenderThread.sendMail(mailObj);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return hmiResultObj;
	}
	
	
	@PostMapping(value="/sendBirthdayMail")
	public  @ResponseBody HmiResultObj create(@RequestBody MailObj mailObj ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		mailObj.setToWho(new String[] {mailObj.getToPerson()});
		try {
			MimeMultipart content = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			  
			mainPart.setText(mailObj.getContent(),"UTF-8", "plain");
			mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
			content.addBodyPart(mainPart);
   
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent( mailObj.getHtmlContent(), "text/html; charset=utf-8" );
			content.addBodyPart(htmlPart);
			
			mailObj.setToFrom(loginSession.getUser().getUserEmail());
			
			mailObj.setMultipartMessage(content);
			
			
			
			hmiResultObj=mailSenderThread.sendMail(mailObj);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return hmiResultObj;
	}
}
