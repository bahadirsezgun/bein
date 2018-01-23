package com.beinplanner.contollers.staff;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	
	@PostMapping(value="/findById/{userId}")
	public  @ResponseBody HmiResultObj doLogin(@PathVariable long userId,HttpServletRequest request ) {
		User user=userService.findUserById(userId);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(user);
		return hmiResultObj;
	}
	
	@PostMapping(value="/findAllSchedulerStaff")
	public  @ResponseBody HmiResultObj findAll(HttpServletRequest request ) {
		List<User> user=userService.findAllByFirmIdAndUserType(loginSession.getUser().getFirmId(),UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(user);
		return hmiResultObj;
	}
	
	@PostMapping(value="/findAllStaff")
	public  @ResponseBody HmiResultObj findAllStaff() {
		List<User> user=userService.findAllStaffByFirmId(loginSession.getUser().getFirmId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultObj(user);
		return hmiResultObj;
	}
	
	@PostMapping(value="/create")
	public  @ResponseBody HmiResultObj create(@RequestBody User user ) {
		user.setFirmId(loginSession.getUser().getFirmId());
		return userService.create(user);
	}
	
	@PostMapping(value="/delete/{userId}")
	public  @ResponseBody HmiResultObj delete(@PathVariable long userId ) {
		return userService.delete(userId);
	}
	
	
	@PostMapping(value="/setStaffToPassiveMode/{userId}") 
	public @ResponseBody HmiResultObj setStaffToPassiveMode(@PathVariable("userId") long userId){
		User user=userService.findUserById(userId);
		user.setStaffStatu(1);
		return userService.create(user);
	}
	
	@PostMapping(value="/setStaffToActiveMode/{userId}") 
	public @ResponseBody HmiResultObj setStaffToActiveMode(@PathVariable("userId") long userId){
		User user=userService.findUserById(userId);
		user.setStaffStatu(0);
		return userService.create(user);
	}
	
	@PostMapping(value="/setPersonalBonusType/{userId}/{bonusTypeP}")
	public @ResponseBody HmiResultObj setPersonalBonusType(@PathVariable("userId") long userId,@PathVariable("bonusTypeP") int bonusTypeP) {
		User user=userService.findUserById(userId);
		user.setBonusTypeP(bonusTypeP);
		return userService.create(user);
	}
	
	@PostMapping(value="/setClassBonusType/{userId}/{bonusTypeC}")
	public @ResponseBody HmiResultObj setClassBonusType(@PathVariable("userId") long userId,@PathVariable("bonusTypeC") int bonusTypeC) {
		User user=userService.findUserById(userId);
		user.setBonusTypeC(bonusTypeC);
		return userService.create(user);
		
	}
	
}
