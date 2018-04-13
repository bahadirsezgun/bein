package com.beinplanner.contollers.register;

import java.util.Date;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.mail.MailObj;
import tr.com.beinplanner.mail.MailSenderThread;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.ResultStatuObj;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	MailSenderThread mailSenderThread;
	
	
	@PostMapping(value="/find")
	public HmiResultObj find(@RequestBody DefFirm defFirm) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
		
		DefFirm df=definitionService.findFirmByEMail(defFirm.getFirmEmail());
		if(df!=null) {
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultObj(df);
		}
		
		return hmiResultObj;
	}
	
	@PostMapping(value="/create")
	public HmiResultObj create(@RequestBody DefFirm defFirm) {
		
		HmiResultObj hmiResultObj=controlAttributes(defFirm);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS_STR) {
		  
			if(userService.findUserByUserEmail(defFirm.getFirmEmail())!=null) {
				
				hmiResultObj.setResultMessage("userFoundWithThisEmail");
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
	    		return hmiResultObj;
			}else {
			
				DefFirm df=definitionService.findFirmByEMail(defFirm.getFirmEmail());
				if(df!=null) {
						try {
							defFirm.setFirmRestriction(df.getFirmRestriction());
							
						  defFirm.setFirmId(df.getFirmId());
						  defFirm.setStripeCustId(df.getStripeCustId());
						  defFirm.setFirmApproved(1);
						  defFirm.setFirmGroupId(0);
						  defFirm.setCreateTime(new Date());
						  defFirm= definitionService.createFirm(defFirm);
						
							User user=new User();
							
							String[] authPerson=defFirm.getFirmAuthPerson().split(" ");
							
							if(authPerson.length>1) {
								user.setUserName(authPerson[0]);
								user.setUserSurname(authPerson[1]);
								
								
								
							}else {
								user.setUserName(authPerson[0]);
								user.setUserSurname("");
							}
							
							user.setFirmId(defFirm.getFirmId());
							user.setUserEmail(defFirm.getFirmEmail());
							user.setUserType(UserTypes.USER_TYPE_ADMIN_INT);
							user.setUserGsm(defFirm.getFirmPhone());
							user.setUserBirthday(new Date());
							user.setUserComment("");
							user.setBonusTypeC(0);
							user.setBonusTypeP(0);
							
							
							
							user=(User)userService.create(user).getResultObj();
						String messageStr="<h4>Dear "+defFirm.getFirmAuthPerson()+"</h4><br>"
								+" Your username is <strong>"+defFirm.getFirmEmail()+"</strong> and your password is <strong>"+user.getPassword()+"</strong><br>"
								+" Thank you for using beinplanner<br>"
								+" Do not hesitate yourself to ask any question to us.<br>"
								+" Best Regards <br>"
								+ " <img style='width:100px;height:auto' src='http://www.beinplanner.com/images/logos/beinplanner.png'/>";
						
							MailObj mailObj=new MailObj();
							
							mailObj.setHtmlContent(messageStr);
							
							
							mailObj.setSubject("Beinplanner Account Information");
							String[] toWho=new String[]{defFirm.getFirmEmail()};
							mailObj.setToWho(toWho);
							
							MimeMultipart content = new MimeMultipart();
							MimeBodyPart mainPart = new MimeBodyPart();
							  
						    mainPart.setText("","UTF-8", "plain");
						    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
						    content.addBodyPart(mainPart);
						   
						    MimeBodyPart htmlPart = new MimeBodyPart();
							htmlPart.setContent( mailObj.getHtmlContent(), "text/html; charset=utf-8" );
							content.addBodyPart(htmlPart);
							
							mailObj.setMultipartMessage(content);
							
							
							hmiResultObj=mailSenderThread.sendMail(mailObj);
							hmiResultObj.setResultMessage("passwordSendToYourMail");
					} catch (Exception e) {
						hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
						hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
						
					}
				}
				else {
					hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
					hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
				}
			}
		    return hmiResultObj;
		}else {
			return hmiResultObj;
		}
		
		
		
	}
	
	private HmiResultObj controlAttributes( DefFirm defFirm) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		if(defFirm.getFirmName().equals("")){
			hmiResultObj.setResultMessage("addNewFirmPH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	
    	if(defFirm.getFirmAuthPerson().equals("")){
    		hmiResultObj.setResultMessage("enterFirmAuthPersonPH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	if(defFirm.getFirmPhone().equals("")){
    		hmiResultObj.setResultMessage("enterFirmPhonePH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	if(defFirm.getFirmEmail().equals("")){
    		hmiResultObj.setResultMessage("enterFirmEmailPH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	if(defFirm.getFirmCityName().equals("")){
    		hmiResultObj.setResultMessage("enterCityNamePH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	if(defFirm.getFirmStateName().equals("")){
    		hmiResultObj.setResultMessage("enterStateNamePH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	if(defFirm.getFirmAddress().equals("")){
    		hmiResultObj.setResultMessage("enterFirmAddressPH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
    	
    	if(defFirm.getFirmRestriction()==0){
    		hmiResultObj.setResultMessage("enterFirmRestrictionPH");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
    		return hmiResultObj;
    	}
		
		
		return hmiResultObj;
		
		
	}
	
}
