package tr.com.beinplanner.schedule.service;

import java.util.Date;
import java.util.List;

import tr.com.beinplanner.packetsale.dao.PacketSaleFactory;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;

public interface IScheduleService {

	public ScheduleTimePlan findScheduleTimePlanPlanByDateTimeForStaff(long schStaffId, Date startDate);
	
	public List<ScheduleTimePlan> findScheduleTimePlansPlanByDatesForStaff(long schStaffId, Date startDate, Date endDate,int firmId);
	
	public List<ScheduleFactory> findScheduleUsersPlanBySaleId(long saleId);
	
	public List<ScheduleFactory> findScheduleUsersPlanBySchtId(long schtId);
	
	
	public HmiResultObj createPlan(ScheduleTimePlan scheduleTimePlan);
	
	public ScheduleTimePlan findScheduleTimePlanBySaleId(PacketSaleFactory psf);
}
