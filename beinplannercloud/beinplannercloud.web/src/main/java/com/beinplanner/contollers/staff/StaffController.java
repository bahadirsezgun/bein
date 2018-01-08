package com.beinplanner.contollers.staff;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/staff")
public class StaffController {

	@Autowired
	UserService userService;
	
	@Autowired
	LoginSession loginSession;
	
	
	@PostMapping(value="/findAllSchedulerStaff")
	public  @ResponseBody HmiResultObj findAll(HttpServletRequest request ) {
		List<User> user=userService.findAllByFirmIdAndUserType(loginSession.getUser().getFirmId(),UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(user);
		return hmiResultObj;
	}
}
