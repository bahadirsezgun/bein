package com.beinplanner.contollers.staff;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.program.dao.ProgramClass;
import tr.com.beinplanner.program.dao.ProgramFactory;
import tr.com.beinplanner.program.dao.ProgramPersonal;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.businessEntity.ScheduleSearchObj;
import tr.com.beinplanner.schedule.businessEntity.StaffClassPlans;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.service.IScheduleService;
import tr.com.beinplanner.schedule.service.ScheduleClassService;
import tr.com.beinplanner.schedule.service.SchedulePersonalService;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.OhbeUtil;
import tr.com.beinplanner.util.ProgramTypes;
import tr.com.beinplanner.util.UserTypes;

@RestController
@RequestMapping("/bein/staff")
public class StaffController {

	@Autowired
	UserService userService;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	IScheduleService iScheduleService;
	
	
	@PostMapping(value="/getSchStaffPlan") 
	public @ResponseBody List<StaffClassPlans> getSchStaffPlanByMonth(@RequestBody ScheduleSearchObj scheduleSearchObj) {
	
		
	
		String payDateStr="";
		
		Date startDate=new Date();
		Date endDate=new Date();
		
		
		if(scheduleSearchObj.getQueryType()==1) {
			String monthStr=""+scheduleSearchObj.getMonth();
			if(scheduleSearchObj.getMonth()<10)
				 monthStr="0"+scheduleSearchObj.getMonth();
			
			 payDateStr="01/"+monthStr+"/"+scheduleSearchObj.getYear()+" 00:00";
			 startDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
			 endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
				
		}else {
			 startDate=OhbeUtil.getThatDayFormatNotNull(scheduleSearchObj.getStartDateStr(), "dd/MM/yyyy HH:mm");
			 endDate=OhbeUtil.getThatDayFormatNotNull(scheduleSearchObj.getEndDateStr(), "dd/MM/yyyy HH:mm");
			
		}
		
		List<StaffClassPlans> staffClassPlans=new ArrayList<StaffClassPlans>();
		List<ScheduleTimePlan> scheduleTimePlanObjs=null;
		
		if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_PERSONAL){
			iScheduleService=schedulePersonalService;
		}else if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_CLASS){
			iScheduleService=scheduleClassService;
		}
		
		scheduleTimePlanObjs=iScheduleService.findScheduleTimePlansPlanByDatesForStaff(scheduleSearchObj.getStaffId(), startDate, endDate, loginSession.getUser().getFirmId());
		
		String color1="#62cb31";
		String color11="#ffffff";
		String color2="#fedde4";
		String color22="#000000";
		String currentColor=color1;
		String currentColorFont=color11;
		long prevSchtId=0;
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlanObjs) {
			SchedulePlan schedulePlan=scheduleService.findSchedulePlanById(scheduleTimePlan.getSchId());
			List<ScheduleFactory> scheduleFactories=null;
			if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_PERSONAL){
				scheduleFactories=schedulePersonalService.findScheduleUsersPlanBySchtId(scheduleTimePlan.getSchtId());
				ProgramFactory programFactory=programService.findProgramPersonalById(schedulePlan.getProgId());
				scheduleTimePlan.setProgName(((ProgramPersonal)programFactory).getProgName());
			}else {
				scheduleFactories=scheduleClassService.findScheduleUsersPlanBySchtId(scheduleTimePlan.getSchtId());
				ProgramFactory programFactory=programService.findProgramClassById(schedulePlan.getProgId());
				scheduleTimePlan.setProgName(((ProgramClass)programFactory).getProgName());
			}
			
			scheduleTimePlan.setUsers(scheduleFactories);
			
			
			StaffClassPlans staffClassPlan=new StaffClassPlans();
			staffClassPlan.setProgName(scheduleTimePlan.getProgName());
			staffClassPlan.setPlanStartDate(scheduleTimePlan.getPlanStartDate());
			staffClassPlan.setPlanStartDateStr(OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(),loginSession.getPtGlobal().getPtScrDateFormat()));
			staffClassPlan.setPlanStartTimeStr(OhbeUtil.getTimeByFormat(new Timestamp(scheduleTimePlan.getPlanStartDate().getTime()), "HH:mm"));
			staffClassPlan.setPlanDay(DateTimeUtil.getDayNames(staffClassPlan.getPlanStartDate()));
			
			staffClassPlan.setSchf(scheduleTimePlan.getUsers());
			staffClassPlans.add(staffClassPlan);
		}
		return staffClassPlans;
		
	}
	
	
	
	
	
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
