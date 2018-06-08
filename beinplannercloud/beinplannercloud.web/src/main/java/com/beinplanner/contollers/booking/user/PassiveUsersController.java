package com.beinplanner.contollers.booking.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.schedule.service.SchedulePassiveUserService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.util.OhbeUtil;

@RestController
@RequestMapping("/bein/passive/booking")
public class PassiveUsersController {

	
	@Autowired
	LoginSession loginSession;
	
	
	
	@Autowired
	SchedulePassiveUserService schedulePassiveUserService;
	
	@PostMapping(value="/findPassive")
	public List<User> findPassive() {
	     Date startDate=OhbeUtil.getDateForNextDate(new Date(), -15);
		return schedulePassiveUserService.findPassiveUsers(loginSession.getUser().getFirmId(), startDate);
	}
}
