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
import tr.com.beinplanner.mail.service.MailService;
import tr.com.beinplanner.mail.templates.MailTemplates;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/mail")
public class SendMailController {

	@Autowired
	MailSenderThread mailSenderThread;
	
	@Autowired
	MailTemplates mailTemplates;
	
	@Autowired
	MailService mailService;
	
	
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
		hmiResultObj.setResultMessage("");
		 List<User> users=new ArrayList<>();
		if(mailObj.getToPerson().equals("self")) {
			
				User user=new User();
				user.setUserEmail(loginSession.getUser().getUserEmail());
				users.add(user);
				
		}else if(mailObj.getToPerson().equals("all")) {
			List<User> members=userService.findAllByFirmIdAndUserType(loginSession.getUser().getFirmId(), UserTypes.USER_TYPE_MEMBER_INT);
			users=members.stream().filter(mem-> !mem.getUserEmail().equals("")).collect(Collectors.toList());
			
			
		}else {
			
			User user=new User();
			user.setUserEmail(mailObj.getToPerson());
			users.add(user);
		}
		
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
			mailService.setUsers(users);
			mailService.setMailObj(mailObj);
			
			Thread thr=new Thread(mailService);
			thr.start();
			
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			
		} catch (MessagingException e) {
			e.printStackTrace();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
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
			 
			String planningHtml=mailTemplates.getWeeklyPlanningInformation(mailObj);
			mailObj.setHtmlContent(planningHtml);
			mailObj.setContent("");
			
			/*
			mainPart.setText(mailObj.getContent(),"UTF-8", "plain");
			
			mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
			content.addBodyPart(mainPart);
   */
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
