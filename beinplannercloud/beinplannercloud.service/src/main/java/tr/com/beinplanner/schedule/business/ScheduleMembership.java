package tr.com.beinplanner.schedule.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipTimePlanRepository;

@Service
@Qualifier("scheduleMembership")
public class ScheduleMembership implements IScheduleMembership {

	@Autowired
	ScheduleMembershipPlanRepository scheduleMembershipPlanRepository;
	
	@Autowired
	ScheduleMembershipTimePlanRepository scheduleMembershipTimePlanRepository;
	
	
	@Override
	public HmiResultObj createPlan(ScheduleFactory scheduleFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduleMembershipPlan findSchedulePlanBySaleId(long saleId) {
		return scheduleMembershipPlanRepository.findBySaleId(saleId);
	}

}
