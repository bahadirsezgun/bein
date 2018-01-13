package tr.com.beinplanner.schedule.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
@Service
@Qualifier("scheduleClass")
public class ScheduleClass implements ISchedulePersonalClass {

	
	@Autowired
	ScheduleUsersClassPlanRepository scheduleUsersClassPlanRepository;
	
	@Autowired
	SchedulePlanRepository schedulePlanRepository;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	
	@Override
	public HmiResultObj createPlan(ScheduleFactory scheduleFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduleFactory findScheduleFactoryPlanById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId, long saleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj createSchedule(SchedulePlan schedulePlan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj continueSchedule(SchedulePlan schedulePlan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj updateSchedule(SchedulePlan schedulePlan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScheduleFactory> findSchedulesBySchId(long schId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public SchedulePlan findSchedulePlanBySaleId(long saleId) {
		return schedulePlanRepository.findSchedulePlanClassBySaleId(saleId);
	}

	@Override
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory) {
		// TODO Auto-generated method stub
		return null;
	}

}
