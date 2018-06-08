package tr.com.beinplanner.schedule.business;

import java.util.Date;
import java.util.List;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.user.dao.User;

public interface ISchedulePersonalClass {

	public HmiResultObj createPlan(ScheduleFactory scheduleFactory);
	
	public ScheduleFactory findScheduleFactoryPlanById(long id);
	
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId,long saleId);
	
	public HmiResultObj createSchedule(SchedulePlan schedulePlan);
	
	public HmiResultObj continueSchedule(SchedulePlan schedulePlan);
	
	public HmiResultObj updateSchedule(SchedulePlan schedulePlan);
	
	public HmiResultObj updateScheduleTimePlan(ScheduleTimePlan scheduleTimePlan);
	
	
	public List<ScheduleFactory> findSchedulesBySchId(long schId);
	
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId);
	
	public SchedulePlan findSchedulePlanBySaleId(long saleId);
	
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory);
	
	
	public List<User> findPassiveUsers(int firmId,Date startDate);
	
}
