package com.beinplanner.contollers.booking.membership;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.program.dao.ProgramMembership;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipTimePlan;
import tr.com.beinplanner.schedule.facade.ScheduleMembershipFacade;
import tr.com.beinplanner.schedule.service.ScheduleMembershipService;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ProgDurationTypes;
import tr.com.beinplanner.util.ResultStatuObj;

@RestController
@RequestMapping("/bein/membership/booking")
public class MembershipBookingController {

	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	ScheduleMembershipService scheduleMembershipService;
	
	
	
	@Autowired
	ProgramService programService;
	
	@PostMapping(value="/createPlan")
	public HmiResultObj createPlan(@RequestBody ScheduleMembershipPlan scheduleMembershipPlan) {
	    return scheduleMembershipService.createPlan(scheduleMembershipPlan);
	}
	
	@PostMapping(value="/findScheduleFactoryPlanBySaleId/{saleId}")
	public ScheduleFactory findScheduleFactoryPlanBySaleId(@PathVariable("saleId") long saleId) {
		return scheduleMembershipService.findScheduleFactoryPlanBySaleId(saleId);
	}
	
	
	
	@RequestMapping(value="/freezeSchedule", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj freezeSchedule(@RequestBody ScheduleMembershipPlan smp){
		return scheduleMembershipService.freezeSchedule(smp);
	}
	
	
	@RequestMapping(value="/unFreezeSchedule/{smtpId}/{smpId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj unFreezeSchedule(@PathVariable("smtpId") long smtpId,@PathVariable("smpId") long smpId) {
		return scheduleMembershipService.unFreezeSchedule(smtpId, smpId);
	}
	
	
}
