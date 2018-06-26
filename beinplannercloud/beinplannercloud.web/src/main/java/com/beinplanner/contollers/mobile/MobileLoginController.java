package com.beinplanner.contollers.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.beinplanner.contollers.mobile.service.UserLoginControlService;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/mobile/login")
public class MobileLoginController {

	@Autowired
	UserLoginControlService userLoginControlService;
	
	@CrossOrigin(origins = "*")
	@PostMapping(value="/getLoginUser")
	public  @ResponseBody HmiResultObj getLoginUser(@RequestBody User user ) throws CloneNotSupportedException {
	   HmiResultObj hmiResultObj=new HmiResultObj();
	   User u=null;
		try {
			u = userLoginControlService.getUser(user);
		} catch (Exception e) {
			
		}
		
	   if(u!=null) {
		   hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		   hmiResultObj.setResultMessage(""+u.getUserType());
		   System.out.println("USER TYPE = "+u.getUserType());
		   
	   }else {
		   hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
	   }
	  return hmiResultObj;
	}
	
}
