package tr.com.beinplanner.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.schedule.business.ScheduleClass;
import tr.com.beinplanner.schedule.business.ScheduleMembership;
import tr.com.beinplanner.schedule.business.SchedulePersonal;
import tr.com.beinplanner.user.dao.User;

@Service
@Qualifier("schedulePassiveUserService")
public class SchedulePassiveUserService {

	@Autowired
	SchedulePersonal schedulePersonal;
	
	@Autowired
	ScheduleClass scheduleClass;
	
	@Autowired
	ScheduleMembership scheduleMembership;
	
	
	public List<User> findPassiveUsers(int firmId,Date startDate){
		
		List<User> usersPersonal=schedulePersonal.findPassiveUsers(firmId, startDate);
		
		List<User> usersClass=scheduleClass.findPassiveUsers(firmId, startDate);
		
		List<User> usersMembership=scheduleMembership.findPassiveUsers(firmId, startDate);
		
		
		List<User> passusers=new ArrayList<User>();
		
		passusers.addAll(usersPersonal);
		passusers.addAll(usersClass);
		passusers.addAll(usersMembership);
		
		
		return passusers;
	}
	
	
}
