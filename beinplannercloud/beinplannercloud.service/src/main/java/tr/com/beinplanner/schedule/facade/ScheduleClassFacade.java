package tr.com.beinplanner.schedule.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.bonus.service.UserBonusPaymentService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersClassPlanRepository;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("scheduleClassFacade")
public class ScheduleClassFacade implements SchedulePersonalClassFacadeService {

	@Autowired
	LoginSession loginSession;
	
	@Autowired
	UserBonusPaymentService userBonusPaymentService;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	ScheduleUsersClassPlanRepository scheduleUsersClassPlanRepository;
	

	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Override
	public HmiResultObj canScheduleChange(long schtId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj canScheduleTimePlanCreateInChain(ScheduleTimePlan scheduleTimePlan) {
		    HmiResultObj hmiResultObj=new HmiResultObj();
			ScheduleTimePlan schTPP=scheduleTimePlanRepository.findScheduleTimePlanClassPlanByDateTimeForStaff(scheduleTimePlan.getSchtStaffId(), scheduleTimePlan.getPlanStartDate());
			if(schTPP!=null) {
				hmiResultObj.setResultMessage("instructorHavePersonalClass");
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}else {
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			}
			return hmiResultObj;
		
	}

	@Override
	public HmiResultObj canScheduleTimePlanDelete(ScheduleTimePlan scheduleTimePlan) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
