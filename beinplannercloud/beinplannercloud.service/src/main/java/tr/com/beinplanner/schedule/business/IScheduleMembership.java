package tr.com.beinplanner.schedule.business;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;

public interface IScheduleMembership {

	
	public HmiResultObj createPlan(ScheduleFactory scheduleFactory);
	
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory);
	
	public ScheduleMembershipPlan findSchedulePlanBySaleId(long saleId);
	
	
}
