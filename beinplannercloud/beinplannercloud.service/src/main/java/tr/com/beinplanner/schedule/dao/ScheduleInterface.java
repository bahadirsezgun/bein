package tr.com.beinplanner.schedule.dao;

import java.util.List;

import tr.com.beinplanner.result.HmiResultObj;

public interface ScheduleInterface {

	public HmiResultObj createPlan(ScheduleFactory scheduleFactory);
	
	public ScheduleFactory findScheduleFactoryPlanById(long id,int firmId);
	
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId,long saleId);
	
	public HmiResultObj createSchedule(SchedulePlan schedulePlan);
	
	public HmiResultObj continueSchedule(SchedulePlan schedulePlan);
	
	public HmiResultObj updateSchedule(SchedulePlan schedulePlan);
	
	//public HmiResultObj changeSchedule(SchedulePlan schedulePlan);
	
	public List<ScheduleFactory> findSchedulesBySchId(long schId,int firmId);
	
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId,int firmId);
	
	public List<ScheduleFactory> findUserSchedulesTimesBySaleId(long saleId,int firmId);
	
	
	public SchedulePlan findSchedulePlanBySaleId(long saleId,int firmId);
	
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory);
	
	
}
