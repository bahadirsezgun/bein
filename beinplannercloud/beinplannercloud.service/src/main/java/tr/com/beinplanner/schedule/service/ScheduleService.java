package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LastClasses;
import tr.com.beinplanner.dashboard.businessEntity.PlannedClassInfo;
import tr.com.beinplanner.program.dao.ProgramFactory;
import tr.com.beinplanner.program.service.ProgramService;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipTimePlan;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersClassPlan;
import tr.com.beinplanner.schedule.dao.ScheduleUsersPersonalPlan;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipTimePlanRepository;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.DateTimeUtil;
import tr.com.beinplanner.util.OhbeUtil;

@Service
@Qualifier("scheduleService")
public class ScheduleService {

	@Autowired
	SchedulePlanRepository schedulePlanRepository;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	@Autowired
	ScheduleMembershipTimePlanRepository scheduleMembershipTimePlanRepository;
	
	@Autowired
	ScheduleMembershipPlanRepository scheduleMembershipPlanRepository;
	
	@Autowired
	ScheduleUsersClassPlanRepository scheduleUsersClassPlanRepository;
	
	@Autowired
	ScheduleUsersPersonalPlanRepository scheduleUsersPersonalPlanRepository;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	UserService userService;
	
	
	public synchronized SchedulePlan findSchedulePlanById(long schId) {
		return schedulePlanRepository.findOne(schId);
	}
	
	
	public synchronized ScheduleTimePlan findScheduleTimePlanById(long schtId){
		return scheduleTimePlanRepository.findOne(schtId);
	}
	
	public synchronized List<ScheduleTimePlan> findScheduleTimePlanBySchId(long schId){
		return scheduleTimePlanRepository.findBySchId(schId);
	}
	
	public synchronized SchedulePlan createSchedulePlan(SchedulePlan schedulePlan) {
		return schedulePlanRepository.save(schedulePlan);
	}
	
	
	
	
	public synchronized boolean findTimePlanToControlDeleteSchStaff(long staffId) {
		if(scheduleTimePlanRepository.findTimePlanToControlDeleteSchStaff(staffId)==null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public synchronized LastClasses findLastOfClasses(int firmId){
		Date startDateNextWeek=DateTimeUtil.getNextWeekStartDate();
		Date endDateNextWeek=DateTimeUtil.getNextWeekEndDate();
	
		Date startDate=DateTimeUtil.getWeekStartDate();
		Date endDate=DateTimeUtil.getWeekEndDate();
	
		
		List<ScheduleMembershipPlan> scheduleMembershipPlansW=scheduleMembershipPlanRepository.findLastOfClasses(startDate, endDate, firmId);
		scheduleMembershipPlansW.forEach(stfpw->{
			
			User user=userService.findUserById(stfpw.getUserId());
			stfpw.setUser(user);
			
			ProgramFactory programFactory= programService.findProgramMembershipById(stfpw.getProgId());
			if(programFactory!=null)
				stfpw.setProgramFactory(programFactory);
		});
		
		
		/**************************************************************************************************************************************/
		/**************************************************************************************************************************************/
		/*******************************************PERSONAL AND CLASS SCHEDULE TIME PLAN THIS WEEK******************************************/
		/**************************************************************************************************************************************/
		/**************************************************************************************************************************************/
		
		List<ScheduleTimePlan> scheduleTimePlansForPersonalW=scheduleTimePlanRepository.findClassesForPersonal(startDate, endDate, firmId);
		scheduleTimePlansForPersonalW.forEach(stfpw->{
			
			SchedulePlan sp=schedulePlanRepository.findOne(stfpw.getSchId());
			
			List<ScheduleUsersPersonalPlan> supp=scheduleUsersPersonalPlanRepository.findBySchtId(stfpw.getSchtId());
			supp.forEach(sf->{
				User user=userService.findUserById(((ScheduleUsersPersonalPlan)sf).getUserId());
				user.setSuppId(((ScheduleUsersPersonalPlan)sf).getSuppId());
				sf.setUser(user);
			});
			
			List<ScheduleFactory> scheduleFactories=new ArrayList<>();
			scheduleFactories.addAll(supp);
			
			ProgramFactory programFactory= programService.findProgramPersonalById(sp.getProgId());
			if(programFactory!=null)
				stfpw.setProgramFactory(programFactory);
			
			stfpw.setScheduleFactories(scheduleFactories);
		});
		
		List<ScheduleTimePlan> scheduleTimePlansForClassW=scheduleTimePlanRepository.findClassesForClass(startDate, endDate, firmId);
		scheduleTimePlansForClassW.forEach(stfpw->{
			
			List<ScheduleUsersClassPlan> sucp=scheduleUsersClassPlanRepository.findBySchtId(stfpw.getSchtId());
			
			SchedulePlan sp=schedulePlanRepository.findOne(stfpw.getSchId());
			
			sucp.forEach(sf->{
				User user=userService.findUserById(((ScheduleUsersClassPlan)sf).getUserId());
				user.setSucpId(((ScheduleUsersClassPlan)sf).getSucpId());
				sf.setUser(user);
			});
			
			List<ScheduleFactory> scheduleFactories=new ArrayList<>();
			scheduleFactories.addAll(sucp);
			
			ProgramFactory programFactory= programService.findProgramClassById(sp.getProgId());
			if(programFactory!=null)
				stfpw.setProgramFactory(programFactory);
			
			
			stfpw.setScheduleFactories(scheduleFactories);
		});
		
		
		scheduleTimePlansForClassW.addAll(scheduleTimePlansForPersonalW);
		/*********************************************************************************************************************************************/
		/**************************************************************************************************************************************/
		/**************************************************************************************************************************************/
		
		List<ScheduleMembershipPlan> scheduleMembershipPlansNW=scheduleMembershipPlanRepository.findLastOfClasses(startDateNextWeek, endDateNextWeek, firmId);
		
		scheduleMembershipPlansNW.forEach(stfpw->{
			User user=userService.findUserById(stfpw.getUserId());
			stfpw.setUser(user);
			ProgramFactory programFactory= programService.findProgramMembershipById(stfpw.getProgId());
			if(programFactory!=null)
				stfpw.setProgramFactory(programFactory);
		});
		
		
		List<ScheduleTimePlan> scheduleTimePlansForClassNW=scheduleTimePlanRepository.findClassesForClass(startDateNextWeek, endDateNextWeek, firmId);
		
		scheduleTimePlansForClassNW.forEach(stfpw->{
			
			SchedulePlan sp=schedulePlanRepository.findOne(stfpw.getSchId());
			
			
			List<ScheduleUsersClassPlan> sucp=scheduleUsersClassPlanRepository.findBySchtId(stfpw.getSchtId());
			
			sucp.forEach(sf->{
				User user=userService.findUserById(((ScheduleUsersClassPlan)sf).getUserId());
				user.setSucpId(((ScheduleUsersClassPlan)sf).getSucpId());
				sf.setUser(user);
			});
			
			
			List<ScheduleFactory> scheduleFactories=new ArrayList<>();
			scheduleFactories.addAll(sucp);
			ProgramFactory programFactory= programService.findProgramClassById(sp.getProgId());
			if(programFactory!=null)
				stfpw.setProgramFactory(programFactory);
			
			stfpw.setScheduleFactories(scheduleFactories);
		});
		
		List<ScheduleTimePlan> scheduleTimePlansForPersonalNW=scheduleTimePlanRepository.findClassesForPersonal(startDateNextWeek, endDateNextWeek, firmId);
		scheduleTimePlansForPersonalNW.forEach(stfpnw->{
			
			SchedulePlan sp=schedulePlanRepository.findOne(stfpnw.getSchId());
			
			List<ScheduleUsersPersonalPlan> supp=scheduleUsersPersonalPlanRepository.findBySchtId(stfpnw.getSchtId());
			supp.forEach(sf->{
				User user=userService.findUserById(((ScheduleUsersPersonalPlan)sf).getUserId());
				user.setSuppId(((ScheduleUsersPersonalPlan)sf).getSuppId());
				sf.setUser(user);
			});
			List<ScheduleFactory> scheduleFactories=new ArrayList<>();
			scheduleFactories.addAll(supp);
			ProgramFactory programFactory= programService.findProgramPersonalById(sp.getProgId());
			if(programFactory!=null)
				stfpnw.setProgramFactory(programFactory);
			
			stfpnw.setScheduleFactories(scheduleFactories);
		});
		
		
		scheduleTimePlansForClassNW.addAll(scheduleTimePlansForPersonalNW);
		
		LastClasses lastClasses=new LastClasses();
		lastClasses.setStpMTW(scheduleMembershipPlansW);
		lastClasses.setStpMNW(scheduleMembershipPlansNW);
		lastClasses.setStpNW(scheduleTimePlansForClassNW);
		lastClasses.setStpTW(scheduleTimePlansForClassW);
		
		return lastClasses;
		
		
	}
	
	
	public synchronized PlannedClassInfo getPlannedClassInfoForPersonalAndClassAndMembership(int firmId, int year, int month) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String startDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date startDate=OhbeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy HH:mm");
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		
		PlannedClassInfo plannedClassInfo=new PlannedClassInfo();
		
		List<ScheduleTimePlan> scheduleTimePlansForPersonal=scheduleTimePlanRepository.findClassesForPersonal(startDate, endDate, firmId);
		
		List<ScheduleTimePlan> scheduleTimePlansForClass=scheduleTimePlanRepository.findClassesForClass(startDate, endDate, firmId);
		
		List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans=scheduleMembershipTimePlanRepository.findScheduleMembershipTimePlan(startDate, endDate, firmId);
		
		
		plannedClassInfo.setClassCount(scheduleTimePlansForPersonal.size()+scheduleTimePlansForClass.size()+scheduleMembershipTimePlans.size());
		plannedClassInfo.setMonth(month);
		plannedClassInfo.setMonthName(DateTimeUtil.getMonthNamesBySequence(month));
		
		return plannedClassInfo;
	}
	
	
	
}
