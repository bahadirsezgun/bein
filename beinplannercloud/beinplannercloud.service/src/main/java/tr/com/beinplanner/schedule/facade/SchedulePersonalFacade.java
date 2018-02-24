package tr.com.beinplanner.schedule.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.bonus.service.UserBonusPaymentService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleFactory;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.repository.ScheduleTimePlanRepository;
import tr.com.beinplanner.schedule.repository.ScheduleUsersPersonalPlanRepository;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.util.BonusLockUtil;
import tr.com.beinplanner.util.ResultStatuObj;

@Service
@Qualifier("schedulePersonalFacade")
public class SchedulePersonalFacade implements SchedulePersonalClassFacadeService {

	@Autowired
	LoginSession loginSession;
	
	@Autowired
	UserBonusPaymentService userBonusPaymentService;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	ScheduleUsersPersonalPlanRepository scheduleUsersPersonalPlanRepository;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	ScheduleTimePlanRepository scheduleTimePlanRepository;
	
	
	@Autowired
	@Qualifier("scheduleClassFacade")
	SchedulePersonalClassFacadeService schedulePersonalClassFacadeService;
	
	@Override
	public HmiResultObj canScheduleChange(long schtId) {
		// TODO canScheduleChange implementation is not finished yet...
		if(settingsService.findPtLock(loginSession.getUser().getFirmId()).getBonusLock()==BonusLockUtil.BONUS_LOCK_FLAG) {
			
			ScheduleTimePlan scheduleTimePlan=scheduleService.findScheduleTimePlanById(schtId);
			if(scheduleTimePlan!=null) {
				
				//List<UserBonusPaymentPersonal> userBonusPaymentPersonal=//userBonusPaymentService.findUserBonusPaymentPersonalByDate(scheduleTimePlan.getSchtStaffId(), scheduleTimePlan.getPlanStartDate(), scheduleTimePlan.getPlanStartDate());
			}
			
			
		}else {
			
		}
		return null;
	}


	@Override
	public HmiResultObj canScheduleTimePlanCreateInChain(ScheduleTimePlan scheduleTimePlan) {
		// TODO control must be extended if class duration and calendar period not equal each other. 
		HmiResultObj hmiResultObj=schedulePersonalClassFacadeService.canScheduleTimePlanCreateInChain(scheduleTimePlan);
		if(hmiResultObj.getResultStatu().equals(ResultStatuObj.RESULT_STATU_SUCCESS_STR)) {
			ScheduleTimePlan schTPP=scheduleTimePlanRepository.findScheduleTimePlanPersonalPlanByDateTimeForStaff(scheduleTimePlan.getSchtStaffId(), scheduleTimePlan.getPlanStartDate());
			if(schTPP!=null) {
				hmiResultObj.setResultMessage("instructorHavePersonalClass");
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
			return hmiResultObj;
		}else {
			return hmiResultObj;
		}
		
	}


	@Override
	public HmiResultObj canScheduleTimePlanDelete(ScheduleTimePlan scheduleTimePlan) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
