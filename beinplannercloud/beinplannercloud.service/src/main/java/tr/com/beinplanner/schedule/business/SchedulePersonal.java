package tr.com.beinplanner.schedule.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.SchedulePlan;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.repository.SchedulePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
import tr.com.beinplanner.user.dao.User;
import tr.com.beinplanner.user.service.UserService;
import tr.com.beinplanner.util.ProgramTypes;
@Service
@Qualifier("schedulePersonal")
public class SchedulePersonal  implements ISchedulePersonalClass{

	@Autowired
	ScheduleUsersPersonalPlanRepository scheduleUsersPersonalPlanRepository;
	
	@Autowired
	SchedulePlanRepository schedulePlanRepository;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	@Autowired
	UserService userService;
	
	
	@Override
	public HmiResultObj updateScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		
		return null;
	}
	
	@Override
	public HmiResultObj createPlan(ScheduleFactory scheduleFactory) {
		
		return null;
	}

	@Override
	public ScheduleFactory findScheduleFactoryPlanById(long id) {
		
		return null;
	}

	@Override
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId, long saleId) {
		
		return null;
	}

	@Override
	public HmiResultObj createSchedule(SchedulePlan schedulePlan) {
		
		return null;
	}

	@Override
	public HmiResultObj continueSchedule(SchedulePlan schedulePlan) {
		
		return null;
	}

	@Override
	public HmiResultObj updateSchedule(SchedulePlan schedulePlan) {
		
		return null;
	}

	@Override
	public List<ScheduleFactory> findSchedulesBySchId(long schId) {
		
		return null;
	}

	@Override
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId) {
		
		return null;
	}

	

	@Override
	public synchronized SchedulePlan findSchedulePlanBySaleId(long saleId) {
		return schedulePlanRepository.findSchedulePlanPersonalBySaleId(saleId);
	}

	@Override
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory) {
		
		return null;
	}

	@Override
	public List<User> findPassiveUsers(int firmId,Date startDate) {
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleTimePlanRepository.findScheduleTimePlansForPassiveUsers(firmId, ProgramTypes.PROGRAM_PERSONAL, startDate);
		Map<Long,ScheduleTimePlan> scheduleTimePlansPassive=new HashMap<Long, ScheduleTimePlan>();
		
		scheduleTimePlans.stream().forEach(stp->{
			ScheduleTimePlan scheduleTimePlan=scheduleTimePlanRepository.findBindedTimePlanForPassive(stp.getSchId(), startDate);
			if(scheduleTimePlan==null) {
				  if(scheduleTimePlansPassive.get(stp.getSchId())==null)
					scheduleTimePlansPassive.put(stp.getSchId(), stp);
			}
		});
		
		
		 List<User> users=new ArrayList<User>();
		 scheduleTimePlansPassive.forEach((schId,stp)->{
			List<User> us=userService.findMemberInPlanning(schId, ProgramTypes.PROGRAM_PERSONAL);
			 us.forEach(u->{
				 u.setProgType(ProgramTypes.PROGRAM_PERSONAL);
				 u.setEndOfPacketDate(stp.getPlanStartDate());
			 });
			 users.addAll(us);
		 });
		
		return users;
	}

	
	
	
}
