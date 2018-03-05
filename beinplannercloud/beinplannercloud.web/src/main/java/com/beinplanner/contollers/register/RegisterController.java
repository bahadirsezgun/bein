package com.beinplanner.contollers.register;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.settings.dao.PtGlobal;
import tr.com.beinplanner.settings.dao.PtLock;
import tr.com.beinplanner.settings.dao.PtRules;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.BonusLockUtil;
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
	
	
	@PostMapping(value="/create")
	public HmiResultObj create(@RequestBody DefFirm defFirm) {
		
		HmiResultObj hmiResultObj=controlAttributes(defFirm);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS_STR) {
		  
			if(userService.findUserByUserEmail(defFirm.getFirmEmail()).isPresent()) {
				
				hmiResultObj.setResultMessage("userFoundWithThisEmail");
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
	    		return hmiResultObj;
			}else {
			
						try {
						  defFirm.setFirmApproved(0);
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
						String messageStr="Dear "+defFirm.getFirmAuthPerson()+"\n"
								+" Your username is "+defFirm.getFirmEmail()+" And your password is "+user.getPassword()+"\n"
								+" Thank you for using beinplanner\n"
								+" Do not hesitate yourself to ask any question to us."
								+" Best Regards";
						
						System.out.println(messageStr);
						/*
							MailSenderThread mailSenderThread=new MailSenderThread(defFirm.getFirmEmail(), messageStr);
							Thread thr=new Thread(mailSenderThread);
							thr.run();
							*/
						
						
						
						PtGlobal ptGlobal=new PtGlobal();
						ptGlobal.setFirmId(defFirm.getFirmId());
						ptGlobal.setPtCurrency("TL");
						ptGlobal.setPtDateFormat("%d/%m/%Y");
						ptGlobal.setPtStaticIp("127.0.0.1");
						ptGlobal.setPtTz("Europe/Istanbul");
						ptGlobal.setPtLang("tr_TR");
						settingsService.createPtGlobal(ptGlobal);
						
						PtLock ptLock=new PtLock();
						ptLock.setBonusLock(BonusLockUtil.BONUS_LOCK_FLAG);
						ptLock.setFirmId(defFirm.getFirmId());						
						settingsService.createPtLock(ptLock);
						
						
						PtRules pt1=new PtRules();
						pt1.setRuleId(1);
						pt1.setRuleName("noClassBeforePayment");
						pt1.setRuleValue(0);
						pt1.setFirmId(defFirm.getFirmId());
						try {
							pt1=settingsService.createPtRules(pt1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("pt1 :"+pt1.getRuleName());
						
						
						
						PtRules pt2=new PtRules();
						pt2.setRuleId(2);
						pt2.setRuleName("noChangeAfterBonusPayment");
						pt2.setRuleValue(1);
						pt2.setFirmId(defFirm.getFirmId());
						pt2.setPtrId(0);
						try {
							pt2=settingsService.createPtRules(pt2);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("pt2 :"+pt2.getRuleName());
						
						
						PtRules pt3=new PtRules();
						pt3.setRuleId(3);
						pt3.setRuleName("payBonusForConfirmedPayment");
						pt3.setRuleValue(1);
						pt3.setFirmId(defFirm.getFirmId());
						pt3.setPtrId(0);
						pt3=settingsService.createPtRules(pt3);
						System.out.println("pt3 :"+pt3.getRuleName());
						
						PtRules pt4=new PtRules();
						pt4.setRuleId(4);
						pt4.setRuleName("taxRule");
						pt4.setRuleValue(0);
						pt4.setFirmId(defFirm.getFirmId());
						pt4.setPtrId(0);
						settingsService.createPtRules(pt4);
						
						PtRules pt5=new PtRules();
						pt5.setRuleId(5);
						pt5.setRuleName("location");
						pt5.setRuleValue(0);
						pt5.setFirmId(defFirm.getFirmId());
						pt5.setPtrId(0);
						settingsService.createPtRules(pt5);
						
						PtRules pt6=new PtRules();
						pt6.setRuleId(6);
						pt6.setRuleName("notice");
						pt6.setRuleValue(0);
						pt6.setFirmId(defFirm.getFirmId());
						pt6.setPtrId(0);
						settingsService.createPtRules(pt6);
						
						PtRules pt7=new PtRules();
						pt7.setRuleId(7);
						pt7.setRuleName("creditCardCommission");
						pt7.setRuleValue(0);
						pt7.setFirmId(defFirm.getFirmId());
						pt7.setPtrId(0);
						settingsService.createPtRules(pt7);
						
						PtRules pt8=new PtRules();
						pt8.setRuleId(8);
						pt8.setRuleName("creditCardCommissionRate");
						pt8.setRuleValue(0);
						pt8.setFirmId(defFirm.getFirmId());
						pt8.setPtrId(0);
						settingsService.createPtRules(pt8);
						
						PtRules pt9=new PtRules();
						pt9.setRuleId(9);
						pt9.setRuleName("noSaleToPlanning");
						pt9.setRuleValue(1);
						pt9.setFirmId(defFirm.getFirmId());
						pt9.setPtrId(0);
						settingsService.createPtRules(pt9);
						
						
						
					} catch (Exception e) {
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
