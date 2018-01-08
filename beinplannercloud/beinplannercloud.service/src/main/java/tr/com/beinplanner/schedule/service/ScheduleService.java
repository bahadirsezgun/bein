package tr.com.beinplanner.schedule.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.dashboard.businessEntity.LastClasses;
import tr.com.beinplanner.dashboard.businessEntity.PlannedClassInfo;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipTimePlan;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipTimePlanRepository;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
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
	
	public SchedulePlan findSchedulePlanById(long schId) {
		return schedulePlanRepository.findOne(schId);
	}
	
	public List<ScheduleTimePlan> findScheduleTimePlansClassPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		return scheduleTimePlanRepository.findScheduleTimePlansClassPlanByDatesForStaff(schStaffId, startDate, endDate);
	}
	
	public List<ScheduleTimePlan> findScheduleTimePlansPersonalPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId){
		return scheduleTimePlanRepository.findScheduleTimePlansPersonalPlanByDatesForStaff(schStaffId, startDate, endDate);
	}
	
	
	public LastClasses findLastOfClasses(int firmId){
		Date startDateNextWeek=DateTimeUtil.getNextWeekStartDate();
		Date endDateNextWeek=DateTimeUtil.getNextWeekEndDate();
	
		Date startDate=DateTimeUtil.getWeekStartDate();
		Date endDate=DateTimeUtil.getWeekEndDate();
	
		
		List<ScheduleMembershipPlan> scheduleMembershipPlansW=scheduleMembershipPlanRepository.findLastOfClasses(startDate, endDate, firmId);
		
		List<ScheduleTimePlan> scheduleTimePlansForClassW=scheduleTimePlanRepository.findClassesForClass(startDate, endDate, firmId);
		scheduleTimePlansForClassW.forEach(stfpw->{
			stfpw.setScheduleUsersClassPlans(scheduleUsersClassPlanRepository.findBySchtId(stfpw.getSchtId()));
		});
		
		List<ScheduleTimePlan> scheduleTimePlansForPersonalW=scheduleTimePlanRepository.findClassesForPersonal(startDate, endDate, firmId);
		scheduleTimePlansForPersonalW.forEach(stfpw->{
			stfpw.setScheduleUsersPersonalPlans(scheduleUsersPersonalPlanRepository.findBySchtId(stfpw.getSchtId()));
		});
		scheduleTimePlansForClassW.addAll(scheduleTimePlansForPersonalW);
		
		
		List<ScheduleMembershipPlan> scheduleMembershipPlansNW=scheduleMembershipPlanRepository.findLastOfClasses(startDateNextWeek, endDateNextWeek, firmId);
		
		List<ScheduleTimePlan> scheduleTimePlansForClassNW=scheduleTimePlanRepository.findClassesForClass(startDateNextWeek, endDateNextWeek, firmId);
		
		scheduleTimePlansForClassNW.forEach(stfpw->{
			stfpw.setScheduleUsersClassPlans(scheduleUsersClassPlanRepository.findBySchtId(stfpw.getSchtId()));
		});
		
		List<ScheduleTimePlan> scheduleTimePlansForPersonalNW=scheduleTimePlanRepository.findClassesForPersonal(startDateNextWeek, endDateNextWeek, firmId);
		scheduleTimePlansForPersonalNW.forEach(stfpnw->{
			stfpnw.setScheduleUsersPersonalPlans(scheduleUsersPersonalPlanRepository.findBySchtId(stfpnw.getSchtId()));
		});
		
		
		scheduleTimePlansForClassNW.addAll(scheduleTimePlansForPersonalNW);
		
		LastClasses lastClasses=new LastClasses();
		lastClasses.setStpMTW(scheduleMembershipPlansW);
		lastClasses.setStpMNW(scheduleMembershipPlansNW);
		lastClasses.setStpNW(scheduleTimePlansForClassNW);
		lastClasses.setStpTW(scheduleTimePlansForClassW);
		
		return lastClasses;
		
		
	}
	
	
	public PlannedClassInfo getPlannedClassInfoForPersonalAndClassAndMembership(int firmId, int year, int month) {
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
