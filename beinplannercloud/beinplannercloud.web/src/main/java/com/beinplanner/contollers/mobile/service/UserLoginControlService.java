package com.beinplanner.contollers.mobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.beinplanner.security.user.CustomUserDetails;

import tr.com.beinplanner.definition.dao.DefFirm;
import tr.com.beinplanner.definition.service.DefinitionService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.FirmApprovedUtil;

@Service
@Qualifier("userLoginControlService")
public class UserLoginControlService {

	@Autowired
	UserService userService;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	SettingsService settingsService;
	
	
	public boolean controlUser(User user) {
		User u=userService.findUserByUserEmailAndPassword(user.getUserEmail(), user.getMobilePassword());
		if(u!=null)
			return true;
		
		return false;
	}
	
	public User getUser(User user) {
		
		User u= userService.findUserByUserEmailAndPassword(user.getUserEmail(), user.getMobilePassword());
		if(u!=null) {
		loginSession.setUser(u);
	    
	    DefFirm defFirm= definitionService.findFirm(u.getFirmId());
	    if(defFirm.getFirmApproved()==FirmApprovedUtil.FIRM_APPROVED_YES) {
	    	loginSession.setPtGlobal(settingsService.findPtGlobalByFirmId(u.getFirmId()));
	        loginSession.setPtRules(settingsService.findPtRulesByFirmId(u.getFirmId()));
	        loginSession.setDefFirm(defFirm);
	        
	        return u;
	    }else {
	    	loginSession.setUser(null);
	    	return null;
	    }
		}else {
			return null;
		}
		
	}
	
	
	
	
}
