package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;

@Service
@Qualifier("scheduleFactoryService")
public class ScheduleFactoryService {

	@Autowired
	ScheduleUsersClassPlanRepository scheduleUsersClassPlanRepository;
	
	@Autowired
	ScheduleUsersPersonalPlanRepository scheduleUsersPersonalPlanRepository;
	
	
	public List<ScheduleFactory> findScheduleUsersClassPlanBySchtId(long schtId){
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersClassPlanRepository.findBySchtId(schtId));
		
		return scheduleFactories;
	}
	
	public List<ScheduleFactory> findScheduleUsersPersonalPlanBySchtId(long schtId){
		
		List<ScheduleFactory> scheduleFactories=new ArrayList<ScheduleFactory>();
		
		scheduleFactories.addAll(scheduleUsersPersonalPlanRepository.findBySchtId(schtId));
		
		return scheduleFactories;
	}
}
