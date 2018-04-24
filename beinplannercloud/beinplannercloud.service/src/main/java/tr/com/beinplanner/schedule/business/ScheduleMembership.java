package tr.com.beinplanner.schedule.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipPlan;
import tr.com.beinplanner.schedule.dao.ScheduleMembershipTimePlan;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleMembershipTimePlanRepository;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("scheduleMembership")
public class ScheduleMembership implements IScheduleMembership {


	@Autowired
	ScheduleMembershipPlanRepository scheduleMembershipPlanRepository;
	
	@Autowired
	ScheduleMembershipTimePlanRepository scheduleMembershipTimePlanRepository;
	
	@Autowired
	UserService userService;
	
	
	@Override
	public synchronized HmiResultObj createPlan(ScheduleMembershipPlan scheduleMembershipPlan) {
		
		scheduleMembershipPlan=scheduleMembershipPlanRepository.save(scheduleMembershipPlan);
		User user=userService.findUserById(scheduleMembershipPlan.getUserId());
		scheduleMembershipPlan.setUser(user);
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultObj(scheduleMembershipPlan);
		
		return hmiResultObj;
	}

	
	@Override
	public synchronized List<ScheduleMembershipPlan> findSchedulePlanByUserId(long userId) {
		List<ScheduleMembershipPlan> scheduleMembershipPlans=scheduleMembershipPlanRepository.findByUserId(userId);
		if(scheduleMembershipPlans.size()>0) {
		User user=userService.findUserById(userId);
		
		scheduleMembershipPlans.forEach(smp->{
			smp.setUser(user);
		});
		}
		
		return scheduleMembershipPlans;
	}


	@Override
	public synchronized HmiResultObj createTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		scheduleMembershipTimePlan=scheduleMembershipTimePlanRepository.save(scheduleMembershipTimePlan);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultObj(scheduleMembershipTimePlan);
		
		return hmiResultObj;
	}


	@Override
	public synchronized HmiResultObj deleteTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		scheduleMembershipTimePlanRepository.delete(scheduleMembershipTimePlan.getSmtpId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		return hmiResultObj;
	}


	@Override
	public synchronized HmiResultObj deletePlan(ScheduleMembershipPlan scheduleFactory) {
		scheduleMembershipPlanRepository.delete(scheduleFactory.getSmpId());
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		return hmiResultObj;
	}

	@Override
	public synchronized ScheduleMembershipPlan findSchedulePlanBySaleId(long saleId) {
		
		ScheduleMembershipPlan scheduleMembershipPlan=scheduleMembershipPlanRepository.findBySaleId(saleId);
		
		
		
		
		if(scheduleMembershipPlan!=null) {
			User user=userService.findUserById(scheduleMembershipPlan.getUserId());
			scheduleMembershipPlan.setUser(user);
			
			List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans=scheduleMembershipTimePlanRepository.findBySmpId(scheduleMembershipPlan.getSmpId());
			scheduleMembershipPlan.setScheduleMembershipTimePlans(scheduleMembershipTimePlans);
		}
		
		return scheduleMembershipPlanRepository.findBySaleId(saleId);
	}

	

	@Override
	public synchronized ScheduleMembershipPlan findSchedulePlanById(long smpId) {
		ScheduleMembershipPlan scheduleMembershipPlan=scheduleMembershipPlanRepository.findOne(smpId);
		
		if(scheduleMembershipPlan!=null) {
			User user=userService.findUserById(scheduleMembershipPlan.getUserId());
			scheduleMembershipPlan.setUser(user);
			
			List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans=scheduleMembershipTimePlanRepository.findBySmpId(scheduleMembershipPlan.getSmpId());
			scheduleMembershipPlan.setScheduleMembershipTimePlans(scheduleMembershipTimePlans);
		}
		return scheduleMembershipPlan;	
	}
	
	@Override
	public synchronized ScheduleMembershipTimePlan findScheduleTimePlanById(long smtpId) {
		ScheduleMembershipTimePlan scheduleMembershipTimePlan=scheduleMembershipTimePlanRepository.findOne(smtpId);
		return scheduleMembershipTimePlan;	
	}


	@Override
	public synchronized List<ScheduleMembershipTimePlan> findScheduleTimePlanByPlanId(long smpId) {
		return scheduleMembershipTimePlanRepository.findBySmpId(smpId);
	}
	
	
}
