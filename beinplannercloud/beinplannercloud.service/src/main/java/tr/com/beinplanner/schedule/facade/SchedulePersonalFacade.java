package tr.com.beinplanner.schedule.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.beinplanner.bonus.dao.UserBonusPaymentPersonal;
import tr.com.beinplanner.bonus.service.UserBonusPaymentService;
import tr.com.beinplanner.login.session.LoginSession;
import tr.com.beinplanner.result.HmiResultObj;
import tr.com.beinplanner.schedule.dao.ScheduleTimePlan;
import tr.com.beinplanner.schedule.service.ScheduleService;
import tr.com.beinplanner.settings.service.SettingsService;
import tr.com.beinplanner.util.BonusLockUtil;

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
	ScheduleService scheduleService;
	
	
	@Override
	public HmiResultObj canScheduleChange(long schtId) {
		
		if(settingsService.findPtLock(loginSession.getUser().getFirmId()).getBonusLock()==BonusLockUtil.BONUS_LOCK_FLAG) {
			
			ScheduleTimePlan scheduleTimePlan=scheduleService.findScheduleTimePlanById(schtId);
			if(scheduleTimePlan!=null) {
				
				//List<UserBonusPaymentPersonal> userBonusPaymentPersonal=//userBonusPaymentService.findUserBonusPaymentPersonalByDate(scheduleTimePlan.getSchtStaffId(), scheduleTimePlan.getPlanStartDate(), scheduleTimePlan.getPlanStartDate());
			}
			
			
		}else {
			
		}
		
		
		return null;
	}

}
