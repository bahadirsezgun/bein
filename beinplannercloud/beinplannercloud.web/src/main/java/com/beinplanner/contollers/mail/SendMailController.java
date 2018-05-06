package com.beinplanner.contollers.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import tr.com.beinplanner.mail.templates.MailTemplates;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/mail")
public class SendMailController {

	@Autowired
	MailSenderThread mailSenderThread;
	
	@Autowired
	MailTemplates mailTemplates;
	
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	UserService userService;
	
	@PostMapping(value="/sendMarketingMailTest")
	public  @ResponseBody HmiResultObj sendMarketingMailTest(@RequestBody MailObj mailObj ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		mailObj.setToWho(new String[] {"bbcsezgun@gmail.com"});
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
	
	
	@PostMapping(value="/sendMarketingMail")
	public  @ResponseBody HmiResultObj sendMarketingMail(@RequestBody MailObj mailObj ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(mailObj.getToPerson().equals("self")) {
			 mailObj.setToWho(new String[] {loginSession.getUser().getUserEmail()});
		}else if(mailObj.getToPerson().equals("all")) {
			
			List<User> members=userService.findAllByFirmIdAndUserType(loginSession.getUser().getFirmId(), UserTypes.USER_TYPE_MEMBER_INT);
			List<User> membersWithMail=new ArrayList<>();
			
			membersWithMail=members.stream().filter(mem-> mem.getUserEmail().matches("@")).collect(Collectors.toList());
			
			
			String[] memStr=new String[membersWithMail.size()];
			int i=0;
			membersWithMail.stream().forEach(mem->{
				memStr[i]=mem.getUserEmail();
			});
			
			mailObj.setToWho(memStr);
			
		}else {
		   mailObj.setToWho(new String[] {mailObj.getToPerson()});
		}
		
		try {
			MimeMultipart content = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			/* 
			String planningHtml=mailTemplates.getWeeklyPlanningInformation(mailObj);
			mailObj.setHtmlContent(planningHtml);
			mailObj.setContent("");
			*/
			
			
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
	

	@PostMapping(value="/sendPlanningMail")
	public  @ResponseBody HmiResultObj sendPlanningMail(@RequestBody MailObj mailObj ) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		mailObj.setToWho(new String[] {mailObj.getToPerson()});
		try {
			MimeMultipart content = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			 /*
			String planningHtml=mailTemplates.getWeeklyPlanningInformation(mailObj);
			mailObj.setHtmlContent(planningHtml);
			mailObj.setContent("");
			*/
			
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
	public  @ResponseBody HmiResultObj sendBirthdayMail(@RequestBody MailObj mailObj ) {
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
