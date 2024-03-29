package tr.com.beinplanner.schedule.facade;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;

public interface SchedulePersonalClassFacadeService {

	
	public HmiResultObj canScheduleChange(long schtId);
	
	public HmiResultObj canScheduleTimePlanCreateInChain(ScheduleTimePlan scheduleTimePlan,SchedulePlan schedulePlan);
	
	public HmiResultObj canScheduleTimePlanUpdateInChain(ScheduleTimePlan scheduleTimePlan) ;
	
	public HmiResultObj canScheduleTimePlanDelete(ScheduleTimePlan scheduleTimePlan);
}
