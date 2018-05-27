package com.beinplanner.contollers.usertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.settings.dao.PtGlobal;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.user.dao.UserTests;
import tr.com.beinplanner.user.service.UserTestService;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/bein/userTest")
public class UserTestsController {

	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	UserTestService userTestService;
	
	@PostMapping(value="/usertest/create")
	public  @ResponseBody HmiResultObj createUserTest(@RequestBody UserTests userTests) {
		return userTestService.create(userTests);
		
	}
	
	@PostMapping(value="/usertest/delete")
	public  @ResponseBody HmiResultObj deleteUserTest(@RequestBody UserTests userTests) {
		return userTestService.delete(userTests);
		
	}
	
	@PostMapping(value="/usertest/find/{userId}")
	public  @ResponseBody HmiResultObj deleteUserTest(@PathVariable(value="userId") long userId) {
		return userTestService.findUserTests(userId);
		
	}
}
